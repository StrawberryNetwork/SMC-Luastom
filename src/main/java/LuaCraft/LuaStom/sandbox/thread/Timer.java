package LuaCraft.LuaStom.sandbox.thread;

import java.util.concurrent.atomic.AtomicInteger;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import LuaCraft.LuaStom.LuaErrorAssert;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

public class Timer extends LuaTable {
    public Timer() {
        rawset("Simple", new VarArgFunction() {
            @Override
            public LuaValue invoke(Varargs args) {
                int ticks = LuaErrorAssert.checkInt(args.arg(2), "Timer:Simple", 1);
                int reps = LuaErrorAssert.checkInt(args.arg(3), "Timer:Simple", 2);
                LuaValue callback = LuaErrorAssert.checkFunction(args.arg(4), "Timer:Simple", 3);

                AtomicInteger remaining = new AtomicInteger(reps);

                final Task[] task = new Task[1];

                task[0] = MinecraftServer.getSchedulerManager().buildTask(() -> {
                    callback.call();

                    if (reps > 0) {
                        if (remaining.decrementAndGet() <= 0) {
                            task[0].cancel();
                        }
                    }
                })
                .delay(TaskSchedule.tick(ticks))
                .repeat(TaskSchedule.tick(ticks))
                .schedule();

                return LuaValue.NIL;
            }
        });
    }
}