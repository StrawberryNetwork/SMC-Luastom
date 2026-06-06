package LuaCraft.LuaStom.sandbox.world;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import net.minestom.server.instance.Chunk;

public class ChunkLib extends LuaTable {
    private Chunk chunk;

    public ChunkLib(Chunk chunk) {
        this.chunk = chunk;

        rawset("GetX", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return LuaValue.valueOf(chunk.getChunkX());
            }
        });

        rawset("GetZ", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return LuaValue.valueOf(chunk.getChunkZ());
            }
        });
    }

    public Chunk getChunk() {
        return chunk;
    }
}
