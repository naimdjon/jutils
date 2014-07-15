package no.ntnu.utils;


import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static no.ntnu.utils.Col.newList;
import static no.ntnu.utils.Sys.addShutdownHook;


public class TaskDaemon {
    private static final TaskDaemon instance = new TaskDaemon();
    private final ExecutorService taskPool = newCachedThreadPool();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    static {
        shutdownHook();
    }

    private TaskDaemon() {

    }

    public static TaskDaemon getInstance() {
        return instance;
    }

    public static void submit(Runnable runnableTask) {
        instance.runAsync(runnableTask);
    }

    public static void scheduleAtFixedRate(Runnable r, long delay, TimeUnit timeUnit) {
        instance.scheduler.scheduleAtFixedRate(r, 0, delay, timeUnit);
    }

    public static void schedule(Runnable r, long delay, TimeUnit timeUnit) {
        instance.scheduler.schedule(r, delay, timeUnit);
    }

    public void runAsync(Runnable runnableTask) {
        submitTask(runnableTask);
    }

    public void submitTask(Runnable runnableTask) {
        taskPool.submit(runnableTask);
    }

    public static List<Runnable> shutdown() {
        List<Runnable> r1 = instance.scheduler.shutdownNow();
        List<Runnable> r2 = instance.taskPool.shutdownNow();
        List<Runnable> c = newList();
        c.addAll(r1);
        c.addAll(r2);
        Debug.d(" runnables  [%s]", c.size());
        return c;
    }


    @SuppressWarnings("rawtypes")
    private static void shutdownHook() {
        addShutdownHook(
                new Callable() {
                    public Object call() throws Exception {
                        if (instance != null && !instance.taskPool.isShutdown())
                            instance.taskPool.shutdownNow();
                        return null;
                    }
                });
    }
}
