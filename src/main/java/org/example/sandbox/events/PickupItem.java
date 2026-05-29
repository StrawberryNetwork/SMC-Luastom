package org.example.sandbox.events;

import java.util.Map;

import org.example.sandbox.entities.ItemLib;
import org.example.sandbox.entities.LivingEntityLib;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.item.PickupItemEvent;

public class PickupItem {
    GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft PickupItemEvent");

    public PickupItem(Map<String, Globals> allGlobals) {
        globalEventHandler.addListener(PickupItemEvent.class, event -> {
            LuaValue livingEntity = new LivingEntityLib(event.getLivingEntity());
            LuaValue itemEntity = new ItemLib(event.getItemEntity());

            for (Map.Entry<String, Globals> entry : allGlobals.entrySet()) {
                LuaValue serverEvent = entry.getValue().get("ServerEvent");
                LuaValue function = serverEvent.get("OnPickupItem");

                LuaTable luaEventTable = new LuaTable();

                luaEventTable.set("LivingEntity", livingEntity);
                luaEventTable.set("ItemEntity", itemEntity);

                if (!function.isnil() && function.isfunction()) {
                    try {
                        function.call(luaEventTable, livingEntity, itemEntity);
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
