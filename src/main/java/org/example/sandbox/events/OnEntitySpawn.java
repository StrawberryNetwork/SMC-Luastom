package org.example.sandbox.events;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.example.sandbox.entities.EntityLib;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minestom.server.event.entity.EntitySpawnEvent;

public class OnEntitySpawn {
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft EntitySpawnEvent");

    public static void handle(EntitySpawnEvent event, ConcurrentHashMap<String, Globals> allGlobals) {
        LuaValue entity = new EntityLib(event.getEntity());
        for (Map.Entry<String, Globals> entry : allGlobals.entrySet()) {

            LuaValue serverEvent = entry.getValue().get("ServerEvent");
            LuaValue function = serverEvent.get("OnEntitySpawn");

            LuaTable luaEventTable = new LuaTable();
            luaEventTable.set("Entity", entity);

            if (!function.isnil() && function.isfunction()) {
                try {
                    function.call(luaEventTable, entity);
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
