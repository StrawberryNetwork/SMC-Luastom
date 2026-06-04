package LuaCraft.LuaStom.sandbox.world;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator;
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator.PerlinNoiseBuilder;

public class PerlinLib extends LuaTable {
    private PerlinNoiseBuilder builder;
    private PerlinNoiseGenerator generator;

    public LuaValue creator() {
        LuaTable tbl = new LuaTable();

        tbl.set("New", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return new PerlinLib(PerlinNoiseGenerator.newBuilder());
            }
        });

        return tbl;
    }

    public PerlinLib(PerlinNoiseBuilder builder) {
        this.builder = builder;
    }

    public @NonNull PerlinNoiseGenerator getPerlin() {
        if (generator == null) return builder.build();

        return generator;
    }

    public @Nullable PerlinNoiseBuilder getBuilder() {
        return builder;
    }
}
