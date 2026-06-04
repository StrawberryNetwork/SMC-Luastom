package LuaCraft.LuaStom.sandbox.world;

import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import LuaCraft.LuaStom.LuaErrorAssert;
import de.articdive.jnoise.pipeline.JNoise.JNoiseBuilder;

public class JNoiseBuilderLib extends LuaTable {
    private JNoiseBuilder<?> noiseBuilder;

    public JNoiseBuilderLib(JNoiseBuilder<?> noiseBuilder) {
        this.noiseBuilder = noiseBuilder;

        rawset("UseFastSimplex", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue simplex) {
                noiseBuilder.fastSimplex(((FastSimplexLib) simplex).getSimplex());

                return JNoiseBuilderLib.this;
            }
        });

        rawset("UsePerlin", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue perlin) {
                noiseBuilder.perlin(((PerlinLib) perlin).getPerlin());

                return JNoiseBuilderLib.this;
            }
        });

        rawset("Scale", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue scale) {
                Double noiseScale = LuaErrorAssert.checkDouble(scale, "JNoise.Scale", 1);

                noiseBuilder.scale(noiseScale);

                return JNoiseBuilderLib.this;
            }
        });

        rawset("Build", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return new JNoiseGeneratorLib(noiseBuilder.build());
            }
        });
    }

    public @Nullable JNoiseBuilder<?> getBuilder() {
        return noiseBuilder;
    }
}