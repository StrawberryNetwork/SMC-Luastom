package LuaCraft.LuaStom.sandbox.events;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import LuaCraft.LuaStom.sandbox.entities.PlayerLib;
import LuaCraft.LuaStom.sandbox.world.InstanceContainerLib;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;

public class OnAsyncPlayerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft AsyncPlayerConfigurationEvent");

    public static void handle(AsyncPlayerConfigurationEvent event, ConcurrentHashMap<String, Globals> allGlobals) {
        for (Map.Entry<String, Globals> entry : allGlobals.entrySet()) {
            LuaValue player = new PlayerLib(event.getPlayer());

            LuaValue serverEvent = entry.getValue().get("ServerEvent");
            LuaValue function = serverEvent.get("OnAsyncPlayerConfiguration");

            LuaTable luaEventTable = new LuaTable();

            luaEventTable.set("Player", player);
            luaEventTable.set("SetSpawningInstance", new OneArgFunction() {
                @Override
                public LuaValue call(LuaValue instance) {
                    if (instance instanceof InstanceContainerLib) {
                        InstanceContainer container = ((InstanceContainerLib) instance).getContainer();

                        event.setSpawningInstance(container);
                    }

                    return LuaValue.NIL;
                }
            });

            if (!function.isnil() && function.isfunction()) {
                try {
                    function.call(luaEventTable, player);
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
    }
}
