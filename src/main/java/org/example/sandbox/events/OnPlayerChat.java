package org.example.sandbox.events;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.example.sandbox.component.ComponentLib;
import org.example.sandbox.component.ComponentUtil;
import org.example.sandbox.entities.PlayerLib;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minestom.server.event.player.PlayerChatEvent;

public class OnPlayerChat {
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft AsyncPlayerConfigurationEvent");

    public static void handle(PlayerChatEvent event, ConcurrentHashMap<String, Globals> allGlobals) {
        LuaValue player = new PlayerLib(event.getPlayer());
        LuaValue rawMessage = LuaValue.valueOf(event.getRawMessage());
        LuaValue formattedMessage = new ComponentLib(event.getFormattedMessage());

        for (Map.Entry<String, Globals> entry : allGlobals.entrySet()) {
            LuaValue serverEvent = entry.getValue().get("ServerEvent");
            LuaValue function = serverEvent.get("OnPlayerChat");

            LuaTable luaEventTable = new LuaTable();
            luaEventTable.set("Player", player);
            luaEventTable.set("RawMessage", rawMessage);
            luaEventTable.set("FormattedMessage", formattedMessage);
            luaEventTable.set("SetChatFormat", new TwoArgFunction() {
                @Override
                public LuaValue call(LuaValue self, LuaValue message) {
                    event.setFormattedMessage(ComponentUtil.luaValueToComponent(message));

                    return LuaValue.NIL;
                }
            });

            if (!function.isnil() && function.isfunction()) {
                try {
                    function.invoke(LuaValue.varargsOf(new LuaValue[]{luaEventTable, player, rawMessage, formattedMessage}));
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