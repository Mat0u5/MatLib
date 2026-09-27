package net.mat0u5.matlib.utils.other;


import com.google.auto.service.AutoService;
import net.mat0u5.matlib.events.server.ServerTickEvents;
import net.mat0u5.matlib.services.RegistrableServer;

@AutoService(RegistrableServer.class)
public class DefaultTaskScheduler implements RegistrableServer {

    private static final TaskScheduler sharedTaskScheduler = new TaskScheduler();

    public static void scheduleTask(int ticks, Runnable goal) {
        sharedTaskScheduler.scheduleTask(ticks, goal);
    }

    public static void scheduleTask(Time time, Runnable goal) {
        sharedTaskScheduler.scheduleTask(time, goal);
    }

    public static void schedulePriorityTask(int ticks, Runnable goal) {
        sharedTaskScheduler.schedulePriorityTask(ticks, goal);
    }

    public static void schedulePriorityTask(Time time, Runnable goal) {
        sharedTaskScheduler.schedulePriorityTask(time, goal);
    }

    public static void clearTasks() {
        sharedTaskScheduler.clearTasks();
    }

    public static void onTick(boolean gameFrozen) {
        sharedTaskScheduler.onTick(gameFrozen);
    }

    @Override
    public void onRegister() {
        ServerTickEvents.END_TICK.register(server -> {
            //? if < 1.20.3 {
            /*boolean gameFrozen = false;
             *///?} else {
            boolean gameFrozen = server.tickRateManager().isFrozen();
            //?}
            DefaultTaskScheduler.onTick(gameFrozen);
        });
    }
}
