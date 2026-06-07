package LuaCraft.LuaStom.sandbox.world;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;

import LuaCraft.LuaStom.LuaErrorAssert;
import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
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

        rawset("SetBiome", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue tbl, LuaValue biomeKey) {
                chunk.setBiome(
                    LuaErrorAssert.checkInt(tbl.get(1), "Chunk:SetBiome", 1),
                    LuaErrorAssert.checkInt(tbl.get(2), "Chunk:SetBiome", 2),
                    LuaErrorAssert.checkInt(tbl.get(3), "Chunk:SetBiome", 3),
                    MinecraftServer.getBiomeRegistry().getKey(Key.key(LuaErrorAssert.checkString(biomeKey, "Chunk:SetBiome", 4)))
                );
                return ChunkLib.this;
            }
        });
    }

    public Chunk getChunk() {
        return chunk;
    }
}
