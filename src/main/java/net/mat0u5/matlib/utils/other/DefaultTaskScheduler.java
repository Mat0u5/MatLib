package net.mat0u5.matlib.utils.other;


public class DefaultTaskScheduler {

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
}
