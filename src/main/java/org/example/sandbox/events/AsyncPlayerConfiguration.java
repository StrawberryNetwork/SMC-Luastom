package org.example.sandbox.events;

import java.util.Map;

import org.example.sandbox.entities.PlayerLib;
import org.example.sandbox.world.InstanceContainerLib;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;

public class AsyncPlayerConfiguration  {
    GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft AsyncPlayerConfigurationEvent");

    public AsyncPlayerConfiguration(Map<String, Globals> allGlobals) {
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            LuaValue player = new PlayerLib(event.getPlayer());
            for (Map.Entry<String, Globals> entry : allGlobals.entrySet()) {
                LuaValue serverEvent = entry.getValue().get("ServerEvent");
                LuaValue function = serverEvent.get("AsyncPlayerConfiguration");

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
        });
    }
}
