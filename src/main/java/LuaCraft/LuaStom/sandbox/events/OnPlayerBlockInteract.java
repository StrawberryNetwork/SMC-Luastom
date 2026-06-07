package LuaCraft.LuaStom.sandbox.events;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.jspecify.annotations.NonNull;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import LuaCraft.LuaStom.sandbox.entities.PlayerLib;
import LuaCraft.LuaStom.sandbox.world.BlockLib;
import net.minestom.server.event.player.PlayerBlockInteractEvent;

public class OnPlayerBlockInteract {
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft PlayerInteractBlockEvent");
    private static final ThreadLocal<@NonNull LuaTable> luaEventTable = ThreadLocal.withInitial(LuaTable::new);
    private static final ThreadLocal<PlayerBlockInteractEvent> currentEvent = new ThreadLocal<>();

    public static void handle(PlayerBlockInteractEvent event, ConcurrentHashMap<String, Globals> allGlobals) {
        currentEvent.set(event);

        LuaValue player = new PlayerLib(event.getPlayer()); 
        LuaValue block = new BlockLib(event.getBlock(), event.getPlayer().getInstance(), event.getBlockPosition());

        LuaTable eventTable = Objects.requireNonNull(luaEventTable.get());

        for (Map.Entry<String, Globals> entry : allGlobals.entrySet()) {
            LuaValue serverEvent = entry.getValue().get("ServerEvent");
            LuaValue function = serverEvent.get("OnPlayerBlockInteract");

            eventTable.set("Player", player);
            eventTable.set("Block", block);

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
