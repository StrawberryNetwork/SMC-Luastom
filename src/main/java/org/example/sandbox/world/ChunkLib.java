package org.example.sandbox.world;

import org.luaj.vm2.LuaTable;

import net.minestom.server.instance.Chunk;

public class ChunkLib extends LuaTable {
    private Chunk chunk;

    public ChunkLib(Chunk chunk) {
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
