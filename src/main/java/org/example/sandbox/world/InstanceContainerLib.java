package org.example.sandbox.world;

import java.io.File;

import org.example.LuaErrorAssert;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

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
    }

    public InstanceContainer getContainer() {
        return this.instanceContainer;
    }
}