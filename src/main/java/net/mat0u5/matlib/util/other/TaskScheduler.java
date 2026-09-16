package net.mat0u5.matlib.util.other;

import net.mat0u5.matlib.MatLib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskScheduler {
    private final List<Task> tasks = new ArrayList<>();
    private final List<Task> newTasks = new ArrayList<>();
    private boolean clearTasks = false;

    public void scheduleTask(int ticks, Runnable goal) {
        if (isDisabled()) return;
        Task task = new Task(ticks, goal);
        newTasks.add(task);
    }

    public void scheduleTask(Time time, Runnable goal) {
        scheduleTask(time.getTicks(), goal);
    }

    public void schedulePriorityTask(int ticks, Runnable goal) {
        Task task = new Task(ticks, goal);
        task.priority = true;
        newTasks.add(task);
    }

    public void schedulePriorityTask(Time time, Runnable goal) {
        schedulePriorityTask(time.getTicks(), goal);
    }

    public void clearTasks() {
        clearTasks = true;
        newTasks.clear();
    }

    public void onTick(boolean gameFrozen) {
        try {
            if (clearTasks) {
                clearTasks = false;
                tasks.clear();
                return;
            }

            Iterator<Task> iterator = tasks.iterator();

            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (!gameFrozen || task.priority) {
                    task.tickCount--;

                    if (task.tickCount <= 0) {
                        try {
                            //Inner try-catch to prevent errors from preventing the task from being removed
                            if (!isDisabled() || task.priority) {
                                task.goal.run();
                            }
                        }catch(Exception e) {
                            MatLib.LOGGER.error("Fatal error while running task " + task);
                            e.printStackTrace();
                        }
                        iterator.remove();
                    }
                }
            }

            tasks.addAll(newTasks);
            newTasks.clear();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean isDisabled() {
        return false;
    }

    public static class Task {
        private int tickCount;
        private final Runnable goal;
        public boolean priority = false;

        public Task(int tickCount, Runnable goal) {
            this.tickCount = tickCount;
            this.goal = goal;
        }
    }
}
