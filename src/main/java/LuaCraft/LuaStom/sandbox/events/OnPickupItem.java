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

import LuaCraft.LuaStom.sandbox.entities.ItemLib;
import LuaCraft.LuaStom.sandbox.entities.LivingEntityLib;
import net.minestom.server.event.item.PickupItemEvent;

public class OnPickupItem {
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft PickupItemEvent");
    private static final ThreadLocal<@NonNull LuaTable> luaEventTable = ThreadLocal.withInitial(LuaTable::new);
    private static final ThreadLocal<PickupItemEvent> currentEvent = new ThreadLocal<>();

    public static void handle(PickupItemEvent event, ConcurrentHashMap<String, Globals> allGlobals) {
        currentEvent.set(event);

        LuaValue livingEntity = new LivingEntityLib(event.getLivingEntity());
        LuaValue itemEntity = new ItemLib(event.getItemEntity());

        LuaTable eventTable = Objects.requireNonNull(luaEventTable.get());

        for (Map.Entry<String, Globals> entry : allGlobals.entrySet()) {
            LuaValue serverEvent = entry.getValue().get("ServerEvent");
                LuaValue function = serverEvent.get("OnPickupItem");

                eventTable.set("LivingEntity", livingEntity);
                eventTable.set("ItemEntity", itemEntity);

                if (!function.isnil() && function.isfunction()) {
                    try {
                        function.invoke(LuaValue.varargsOf(new LuaValue[]{serverEvent, eventTable, livingEntity, itemEntity}));
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
