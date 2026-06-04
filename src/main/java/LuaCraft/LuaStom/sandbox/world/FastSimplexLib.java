package LuaCraft.LuaStom.sandbox.world;

import org.jspecify.annotations.NonNull;
import org.luaj.vm2.LuaTable;

import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator;

public class FastSimplexLib extends LuaTable {
    private FastSimplexNoiseGenerator generator;

    public FastSimplexLib(FastSimplexNoiseGenerator generator) {
        this.generator = generator;
    }

    public @NonNull FastSimplexNoiseGenerator getSimplex() {
        if (generator == null) return FastSimplexNoiseGenerator.newBuilder().build();

        return generator;
    }
}
