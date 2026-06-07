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

import LuaCraft.LuaStom.sandbox.component.ComponentLib;
import LuaCraft.LuaStom.sandbox.component.ComponentUtil;
import LuaCraft.LuaStom.sandbox.entities.PlayerLib;
import net.minestom.server.event.player.PlayerChatEvent;

public class OnPlayerChat {
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft AsyncPlayerConfigurationEvent");
    private static final ThreadLocal<@NonNull LuaTable> luaEventTable = ThreadLocal.withInitial(LuaTable::new);
    private static final ThreadLocal<PlayerChatEvent> currentEvent = new ThreadLocal<>();

    private static final TwoArgFunction setChatFormatting = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue self, LuaValue message) {
            PlayerChatEvent event = currentEvent.get();

            event.setFormattedMessage(ComponentUtil.luaValueToComponent(message));

            return LuaValue.NIL;
        }
    };

    public static void handle(PlayerChatEvent event, ConcurrentHashMap<String, Globals> allGlobals) {
        currentEvent.set(event);

        LuaValue player = new PlayerLib(event.getPlayer());
        LuaValue rawMessage = LuaValue.valueOf(event.getRawMessage());
        LuaValue formattedMessage = new ComponentLib(event.getFormattedMessage());

        LuaTable eventTable = Objects.requireNonNull(luaEventTable.get());

        for (Map.Entry<String, Globals> entry : allGlobals.entrySet()) {
            LuaValue serverEvent = entry.getValue().get("ServerEvent");
            LuaValue function = serverEvent.get("OnPlayerChat");

            eventTable.set("Player", player);
            eventTable.set("RawMessage", rawMessage);
            eventTable.set("FormattedMessage", formattedMessage);
            eventTable.set("SetChatFormat", setChatFormatting);

            if (!function.isnil() && function.isfunction()) {
                try {
                    function.invoke(LuaValue.varargsOf(new LuaValue[]{serverEvent, eventTable, player, rawMessage, formattedMessage}));
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