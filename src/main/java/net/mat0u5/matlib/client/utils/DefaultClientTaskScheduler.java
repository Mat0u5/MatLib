package net.mat0u5.matlib.client.utils;


import com.google.auto.service.AutoService;
import net.mat0u5.matlib.client.events.ClientLocalPlayerEvents;
import net.mat0u5.matlib.client.services.RegistrableClient;
import net.mat0u5.matlib.utils.other.TaskScheduler;
import net.mat0u5.matlib.utils.other.Time;

@AutoService(RegistrableClient.class)
public class DefaultClientTaskScheduler implements RegistrableClient {

    private static final TaskScheduler sharedClientTaskScheduler = new TaskScheduler();

    public static void scheduleTask(int ticks, Runnable goal) {
        sharedClientTaskScheduler.scheduleTask(ticks, goal);
    }

    public static void scheduleTask(Time time, Runnable goal) {
        sharedClientTaskScheduler.scheduleTask(time, goal);
    }

    public static void schedulePriorityTask(int ticks, Runnable goal) {
        sharedClientTaskScheduler.schedulePriorityTask(ticks, goal);
    }

    public static void schedulePriorityTask(Time time, Runnable goal) {
        sharedClientTaskScheduler.schedulePriorityTask(time, goal);
    }

    public static void clearTasks() {
        sharedClientTaskScheduler.clearTasks();
    }

    public static void onTickClient() {
        sharedClientTaskScheduler.onTick(false);
    }

    @Override
    public void onRegister() {
        ClientLocalPlayerEvents.END_TICK.register(player -> DefaultClientTaskScheduler.onTickClient());
    }
}
