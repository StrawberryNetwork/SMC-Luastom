package LuaCraft.LuaStom.sandbox.thread;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import LuaCraft.LuaStom.LuaErrorAssert;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Scheduler;

public class RunSync extends TwoArgFunction {
    private final Object LuaLock = new Object();
    Scheduler scheduler = MinecraftServer.getSchedulerManager();

    @Override
    public LuaValue call(LuaValue self, LuaValue function) {
        synchronized (LuaLock) {
            LuaErrorAssert.checkFunction(function, "Sync", 1).call();
        }

        return LuaValue.NIL;
    }
}