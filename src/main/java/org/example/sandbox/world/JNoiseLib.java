package org.example.sandbox.world;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import de.articdive.jnoise.pipeline.JNoise;
import de.articdive.jnoise.pipeline.JNoise.JNoiseBuilder;

public class JNoiseLib extends LuaTable {
    private JNoiseBuilder<?> noiseBuilder;
    private JNoise noise;

    public JNoiseLib(JNoiseBuilder<?> noiseBuilder) {
        this.noiseBuilder = noiseBuilder;

        rawset("UseFastSimplex", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue simplex) {
                noiseBuilder.fastSimplex(((FastSimplexLib) simplex).getSimplex());

                return JNoiseLib.this;
            }
        });

        rawset("UsePerlin", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue perlin) {
                noiseBuilder.perlin(((PerlinLib) perlin).getPerlin());

                return JNoiseLib.this;
            }
        });
    }

    public @NonNull JNoise getNoise() {
        if (noise == null) return noiseBuilder.build();

        return noise;
    }

    public @Nullable JNoiseBuilder<?> getBuilder() {
        return noiseBuilder;
    }
}