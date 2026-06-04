package org.example.sandbox.world;

import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class BlockLib extends LuaTable {
    public BlockLib(Block block, @Nullable Instance instance, BlockVec position) {
        rawset("GetName", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return LuaValue.valueOf(block.name());
            }
        });

        rawset("GetChunk", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return new ChunkLib(instance.getChunk(position.chunkX(), position.chunkZ()));
            }
        });
    }
}