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

import LuaCraft.LuaStom.sandbox.world.ChunkLib;
import net.minestom.server.event.instance.InstanceChunkLoadEvent;

public class OnChunkLoad {
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft ChunkLoadEvent");

    private static final ThreadLocal<@NonNull LuaTable> luaEventTable = ThreadLocal.withInitial(LuaTable::new);
    private static final ThreadLocal<InstanceChunkLoadEvent> currentEvent = new ThreadLocal<>();

    public static void handle(InstanceChunkLoadEvent event, ConcurrentHashMap<String, Globals> allGlobals) {
        currentEvent.set(event);

        LuaValue chunk = new ChunkLib(event.getChunk());

        for (Map.Entry<String, Globals> entry : allGlobals.entrySet()) {

            LuaValue serverEvent = entry.getValue().get("ServerEvent");
            LuaValue function = serverEvent.get("OnChunkLoad");

            LuaTable eventTable = Objects.requireNonNull(luaEventTable.get());
            eventTable.set("Chunk", chunk);

            if (!function.isnil() && function.isfunction()) {
                try {
                    function.call(serverEvent, eventTable, chunk);
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
