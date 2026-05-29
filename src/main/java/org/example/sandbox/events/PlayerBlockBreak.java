package org.example.sandbox.events;

import java.util.Map;

import org.example.sandbox.entities.PlayerLib;
import org.example.sandbox.position.BlockVecLib;
import org.example.sandbox.world.BlockLib;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockBreakEvent;

public class PlayerBlockBreak {
    GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft PlayerBlockBreakEvent");

    public PlayerBlockBreak(Map<String, Globals> allGlobals) {
        globalEventHandler.addListener(PlayerBlockBreakEvent.class, event -> {
            LuaValue player = new PlayerLib(event.getPlayer());
            LuaValue block = new BlockLib(event.getBlock());
            LuaValue blockPosition = new BlockVecLib(event.getBlockPosition());

            for (Map.Entry<String, Globals> entry : allGlobals.entrySet()) {
                LuaValue serverEvent = entry.getValue().get("ServerEvent");
                LuaValue function = serverEvent.get("OnPlayerBlockBreak");

                LuaTable luaEventTable = new LuaTable();

                luaEventTable.set("Player", player);
                luaEventTable.set("Block", block);
                luaEventTable.set("BlockPosition", blockPosition);

                if (!function.isnil() && function.isfunction()) {
                    try {
                        function.invoke(LuaValue.varargsOf(new LuaValue[]{luaEventTable, player, block, blockPosition}));
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
