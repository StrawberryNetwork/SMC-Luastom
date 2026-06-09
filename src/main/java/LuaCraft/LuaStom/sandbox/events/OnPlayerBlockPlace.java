package LuaCraft.LuaStom.sandbox.events;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.jspecify.annotations.NonNull;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import LuaCraft.LuaStom.LuaErrorAssert;
import LuaCraft.LuaStom.sandbox.entities.PlayerLib;
import LuaCraft.LuaStom.sandbox.position.PointLib;
import LuaCraft.LuaStom.sandbox.world.BlockLib;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;

public class OnPlayerBlockPlace {
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft PlayerInteractBlockEvent");
    private static final ThreadLocal<@NonNull LuaTable> luaEventTable = ThreadLocal.withInitial(LuaTable::new);
    private static final ThreadLocal<PlayerBlockPlaceEvent> currentEvent = new ThreadLocal<>();

    private static final TwoArgFunction setCancelled = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue self, LuaValue isCancelled) {
            PlayerBlockPlaceEvent event = currentEvent.get();
            
            event.setCancelled(!LuaErrorAssert.checkBoolean(isCancelled, "Event:ShouldPlace", 1));

            return LuaValue.NIL;
        }
    };

    private static final TwoArgFunction setBlock = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue self, LuaValue block) {
            PlayerBlockPlaceEvent event = currentEvent.get();

            event.setBlock(((BlockLib) block).getBlock());

            return LuaValue.NIL;
        }
    };

    public static void handle(PlayerBlockPlaceEvent event, ConcurrentHashMap<String, Globals> allGlobals) {
        currentEvent.set(event);

        LuaValue player = new PlayerLib(event.getPlayer());
        LuaValue block = new BlockLib(event.getBlock(), event.getPlayer().getInstance(), event.getBlockPosition());
        LuaValue blockPos = new PointLib(event.getBlockPosition());
        LuaTable eventTable = Objects.requireNonNull(luaEventTable.get());

        for (Map.Entry<String, Globals> entry : allGlobals.entrySet()) {
            LuaValue serverEvent = entry.getValue().get("ServerEvent");
            LuaValue function = serverEvent.get("OnPlayerBlockPlace");

            eventTable.set("Player", player);
            eventTable.set("Block", block);
            eventTable.set("ShouldPlace", setCancelled);
            eventTable.set("SetBlock", setBlock);
            eventTable.set("BlockPosition", blockPos);

            if (!function.isnil() && function.isfunction()) {
                    try {
                        function.invoke(LuaValue.varargsOf(new LuaValue[]{serverEvent, eventTable, player, block}));
                    } catch (LuaError e) {
                        String baseMsg = e.getMessage();
                        String trueLocation = "";

                        for (StackTraceElement element : e.getStackTrace()) {
                            String fileName = element.getFileName();
                            if (fileName != null && fileName.endsWith(".lua")) {
                                trueLocation = fileName + " related to line number " + element.getLineNumber();
                                break;
                            }
                        }

                        if (!trueLocation.isEmpty()) {
                            if (baseMsg != null && baseMsg.contains("?")) {
                                baseMsg = trueLocation + ": " + baseMsg;
                            } else {
                                baseMsg = trueLocation + ": " + baseMsg;
                            }
                        }

                        logger.error(baseMsg);
                    }
                }
            }
        currentEvent.remove();
    }
}
