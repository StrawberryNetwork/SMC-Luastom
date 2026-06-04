package org.example.sandbox.world;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import net.minestom.server.instance.ChunkLoader;

public class ChunkLoaderLib extends LuaTable {
    public ChunkLoaderLib(ChunkLoader loader) {
        rawset("SaveChunk", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue luaChunk) {
                if (luaChunk instanceof ChunkLib) {
                    loader.saveChunk(((ChunkLib) luaChunk).getChunk());
                }
                
                return ChunkLoaderLib.this;
            }
        });
    }
}