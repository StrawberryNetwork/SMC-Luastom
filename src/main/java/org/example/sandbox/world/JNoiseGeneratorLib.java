package org.example.sandbox.world;

import org.example.LuaErrorAssert;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import de.articdive.jnoise.pipeline.JNoise;

public class JNoiseGeneratorLib extends LuaTable {
    private JNoise noise;

    public JNoiseGeneratorLib(JNoise noise) {
        this.noise = noise;

        rawset("Evaluate", new VarArgFunction() {
            @Override
            public LuaValue invoke(Varargs args) {
                int argCount = args.narg();
                return switch (argCount) {
                    case 2 -> LuaValue.valueOf(
                            noise.evaluateNoise(LuaErrorAssert.checkDouble(args.arg(2), "JNoise.Evaluate", 1)));
                    case 3 -> LuaValue
                            .valueOf(noise.evaluateNoise(LuaErrorAssert.checkDouble(args.arg(1), "JNoise.Evaluate", 1),
                                    LuaErrorAssert.checkDouble(args.arg(2), "JNoise.Evaluate", 2)));
                    case 4 -> LuaValue
                            .valueOf(noise.evaluateNoise(LuaErrorAssert.checkDouble(args.arg(1), "JNoise.Evaluate", 1),
                                    LuaErrorAssert.checkDouble(args.arg(2), "JNoise.Evaluate", 2),
                                    LuaErrorAssert.checkDouble(args.arg(3), "JNoise.Evaluate", 3)));
                    case 5 -> LuaValue
                            .valueOf(noise.evaluateNoise(LuaErrorAssert.checkDouble(args.arg(1), "JNoise.Evaluate", 1),
                                    LuaErrorAssert.checkDouble(args.arg(2), "JNoise.Evaluate", 2),
                                    LuaErrorAssert.checkDouble(args.arg(3), "JNoise.Evaluate", 3),
                                    LuaErrorAssert.checkDouble(args.arg(4), "JNoise.Evaluate", 4)));
                    default -> LuaValue.NIL;
                };
            }
        });
    }

    public JNoise getNoise() {
        return noise;
    }
}
