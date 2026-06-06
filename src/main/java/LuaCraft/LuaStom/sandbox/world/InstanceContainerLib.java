package LuaCraft.LuaStom.sandbox.world;

import java.io.File;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import LuaCraft.LuaStom.LuaErrorAssert;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.DynamicChunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;

public class InstanceContainerLib extends LuaTable {
    private InstanceContainer instanceContainer;
    private String instanceName = "world";

    public InstanceContainerLib(InstanceContainer container) {
        this.instanceContainer = container;

        rawset("UseDefaultLoader", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                container.setChunkLoader(new AnvilLoader("worlds/" + instanceName));
                new File("worlds", instanceName).mkdirs();

                return InstanceContainerLib.this;
            }
        });
        
        rawset("SetWorldName", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue name) {
                instanceName = LuaErrorAssert.checkString(name, "SetWorldName", 1);

                return InstanceContainerLib.this;
            }
        });

        rawset("SetGenerator", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue callback) {
                container.setGenerator(unit -> {
                    LuaErrorAssert.checkFunction(callback, "SetGenerator", 1).call(new GenerationUnitLib(unit));
                });
                return InstanceContainerLib.this;
            }
        });

        rawset("ToggleLighting", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue toggle) {
                if (LuaErrorAssert.checkBoolean(toggle, "ToggleLighting", 1)) {
                    container.setChunkSupplier(LightingChunk::new);
                } else {
                    container.setChunkSupplier(DynamicChunk::new);
                }

                return InstanceContainerLib.this;
            }
        });

        rawset("GetChunkLoader", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return new ChunkLoaderLib(container.getChunkLoader());
            }
        });

        rawset("UnloadChunk", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue chunk) {
                if (chunk instanceof ChunkLib) {
                    container.unloadChunk(((ChunkLib) chunk).getChunk());

                    return InstanceContainerLib.this;
                } else {
                    throw new LuaError("UnloadChunk expects a Chunk, received unknown value");
                }
            }
        });

        rawset("GetChunks", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                LuaTable chunks = new LuaTable();

                for (Chunk chunk : container.getChunks()) {
                    chunks.insert(chunks.length() + 1, new ChunkLib(chunk));
                }

                return chunks;
            }
        });
    }

    public InstanceContainer getContainer() {
        return this.instanceContainer;
    }
}