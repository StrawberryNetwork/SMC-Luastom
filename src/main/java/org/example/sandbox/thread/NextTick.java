package org.example.sandbox.thread;

import org.example.LuaErrorAssert;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

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