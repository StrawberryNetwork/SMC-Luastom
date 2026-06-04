package LuaCraft.LuaStom.sandbox.thread;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import LuaCraft.LuaStom.LuaErrorAssert;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Scheduler;

public class NextTick extends TwoArgFunction {
    Scheduler scheduler = MinecraftServer.getSchedulerManager();

    @Override
    public LuaValue call(LuaValue self, LuaValue function) {
        scheduler.scheduleNextTick(() -> {
            LuaErrorAssert.checkFunction(function, "NextTick", 1).call();
        });
        
        return LuaValue.NIL;
    }
}