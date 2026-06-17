package LuaCraft.LuaStom.sandbox.thread;

import java.util.concurrent.CompletableFuture;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import LuaCraft.LuaStom.LuaErrorAssert;

public class RunAsync extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue self, LuaValue func) {
        CompletableFuture.runAsync(() -> {
            LuaErrorAssert.checkFunction(func, "Async", 1).call();
        });

        return LuaValue.NIL;
    }
}
