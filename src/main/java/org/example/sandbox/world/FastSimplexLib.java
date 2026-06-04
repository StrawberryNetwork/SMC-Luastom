package org.example.sandbox.world;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator;
import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator.FastSimplexNoiseBuilder;

public class FastSimplexLib extends LuaTable {
    private FastSimplexNoiseGenerator generator;
    private FastSimplexNoiseBuilder builder;

    public static LuaValue creator() {
        LuaTable tbl = new LuaTable();

        tbl.set("New", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return new FastSimplexLib(FastSimplexNoiseGenerator.newBuilder());
            }
        });

        return tbl;
    }

    public FastSimplexLib(FastSimplexNoiseBuilder builder) {
        this.builder = builder;
    }

    public @NonNull FastSimplexNoiseGenerator getSimplex() {
        if (generator == null) return builder.build();

        return generator;
    }

    public @Nullable FastSimplexNoiseBuilder getSimplexBuilder() {
        return builder;
    }
}
