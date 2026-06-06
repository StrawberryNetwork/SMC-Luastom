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

import LuaCraft.LuaStom.sandbox.entities.PlayerLib;
import LuaCraft.LuaStom.sandbox.world.InstanceContainerLib;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;

public class OnAsyncPlayerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft AsyncPlayerConfigurationEvent");
    private static final ThreadLocal<@NonNull LuaTable> luaEventTable = ThreadLocal.withInitial(LuaTable::new);
    private static final ThreadLocal<AsyncPlayerConfigurationEvent> currentEvent = new ThreadLocal<>();

    private static final TwoArgFunction setSpawningInstance = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue self, LuaValue instance) {
            AsyncPlayerConfigurationEvent event = currentEvent.get();
            if (instance instanceof InstanceContainerLib) {
                event.setSpawningInstance(((InstanceContainerLib) instance).getContainer());
            }
            return LuaValue.NIL;
        }
    };

    public static void handle(AsyncPlayerConfigurationEvent event, ConcurrentHashMap<String, Globals> allGlobals) {
        currentEvent.set(event);

        LuaValue player = new PlayerLib(event.getPlayer());

        LuaTable eventTable = Objects.requireNonNull(luaEventTable.get());
        eventTable.set("Player", player);
        eventTable.set("SetSpawningInstance", setSpawningInstance);

        for (Map.Entry<String, Globals> entry : allGlobals.entrySet()) {

            LuaValue serverEvent = entry.getValue().get("ServerEvent");
            LuaValue function = serverEvent.get("OnAsyncPlayerConfiguration");

            if (!function.isnil() && function.isfunction()) {
                try {
                    function.call(serverEvent, eventTable, player);
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
