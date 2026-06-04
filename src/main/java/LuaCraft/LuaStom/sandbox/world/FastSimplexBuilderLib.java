package LuaCraft.LuaStom.sandbox.world;

import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator;
import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator.FastSimplexNoiseBuilder;

public class FastSimplexBuilderLib extends LuaTable {
    private FastSimplexNoiseBuilder builder;

    public static LuaValue creator() {
        LuaTable tbl = new LuaTable();

        tbl.set("New", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return new FastSimplexBuilderLib(FastSimplexNoiseGenerator.newBuilder());
            }
        });

        return tbl;
    }

    public FastSimplexBuilderLib(FastSimplexNoiseBuilder builder) {
        this.builder = builder;

        rawset("Build", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return new FastSimplexLib(builder.build());
            }
        });
    }

    public @Nullable FastSimplexNoiseBuilder getSimplexBuilder() {
        return builder;
    }
}
