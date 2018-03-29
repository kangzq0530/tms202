package com.msemu.commons.thread;

import com.msemu.core.configs.TimerConfig;
import com.msemu.core.startup.StartupComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/14.
 */
@StartupComponent("Timer")
public class TimerPool {
    private static final Logger log = LoggerFactory.getLogger(TimerPool.class);
    private static final AtomicReference<TimerPool> instance = new AtomicReference<>();
    private Timer worldTimer;
    private Timer mapTimer;
    private Timer buffTimer;
    private Timer etcTimer;
    private Timer networkTimer;
    private boolean _shutdown = false;

    public TimerPool() {
        this.worldTimer = new Timer(TimerConfig.WORLD_TIMER_CORE_POOL_SIZE * Runtime.getRuntime().availableProcessors(), new TimerPool.PriorityThreadFactory("WorldTimer", 3));
        this.mapTimer = new Timer(TimerConfig.MAP_TIMER_CORE_POOL_SIZE * Runtime.getRuntime().availableProcessors(), new TimerPool.PriorityThreadFactory("WorldTimer", 5));
        this.buffTimer = new Timer(TimerConfig.EFFECT_TIMER_CORE_POOL_SIZE * Runtime.getRuntime().availableProcessors(), new TimerPool.PriorityThreadFactory("WorldTimer", 5));
        this.etcTimer = new Timer(TimerConfig.ETC_TIMER_CORE_POOL_SIZE * Runtime.getRuntime().availableProcessors(), new TimerPool.PriorityThreadFactory("WorldTimer", 1));
        this.networkTimer = new Timer(Runtime.getRuntime().availableProcessors(), new TimerPool.PriorityThreadFactory("NetworkSTPool", 10));
        this.etcTimer.registerInMinute(new TimerPool.PurgeTask(), 5, 5);
        log.info("TimerPool initialized");
    }

    public static TimerPool getInstance() {
        TimerPool value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new TimerPool();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public Timer getWorldTimer() {
        return this.worldTimer;
    }

    public Timer getMapTimer() {
        return this.mapTimer;
    }

    public Timer getBuffTimer() {
        return this.buffTimer;
    }

    public Timer getEtcTimer() {
        return this.etcTimer;
    }

    public Timer getNetworkTimer() {
        return this.networkTimer;
    }

    public boolean isShutdown() {
        return this._shutdown;
    }

    private static class PriorityThreadFactory implements ThreadFactory {
        private final int priority;
        private final String name;
        private final AtomicInteger _threadNumber = new AtomicInteger(1);
        private final ThreadGroup group;

        PriorityThreadFactory(String name, int priority) {
            this.priority = priority;
            this.name = name;
            this.group = new ThreadGroup(this.name);
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread t = new Thread(this.group, runnable, this.name + "-" + this._threadNumber.getAndIncrement());
            t.setPriority(this.priority);
            return t;
        }

        public ThreadGroup getGroup() {
            return this.group;
        }
    }

    public class Timer {
        private final ScheduledThreadPoolExecutor stpe;

        Timer(int corePoolSize,
              ThreadFactory threadFactory) {
            stpe = new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
            stpe.setKeepAliveTime(10, TimeUnit.MINUTES);
            stpe.allowCoreThreadTimeOut(true);
            stpe.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        }

        public ScheduledFuture<?> register(Runnable r, long repeatTime, long delay) {
            try {
                return stpe.scheduleAtFixedRate(r, delay, repeatTime, TimeUnit.MILLISECONDS);
            } catch (RejectedExecutionException except) {
                log.error("Timer register Error", except);
                return null;
            }
        }

        public ScheduledFuture<?> register(Runnable r, long repeatTime) {
            try {
                return stpe.scheduleAtFixedRate(r, 0, repeatTime, TimeUnit.MILLISECONDS);
            } catch (RejectedExecutionException except) {
                log.error("Timer register Error", except);
                return null;
            }
        }

        public ScheduledFuture<?> registerInMinute(Runnable r, long repeatTime, long delay) {
            try {
                return stpe.scheduleAtFixedRate(r, delay, repeatTime, TimeUnit.MINUTES);
            } catch (RejectedExecutionException except) {
                log.error("Timer register Error", except);
                return null;
            }
        }

        public ScheduledFuture<?> registerInMinute(Runnable r, long repeatTime) {
            try {
                return stpe.scheduleAtFixedRate(r, 0, repeatTime, TimeUnit.MINUTES);
            } catch (RejectedExecutionException except) {
                log.error("Timer register Error", except);
                return null;
            }
        }

        public ScheduledFuture<?> schedule(Runnable r, long delay) {
            try {
                return stpe.schedule(r, delay, TimeUnit.MILLISECONDS);
            } catch (RejectedExecutionException except) {
                log.error("Timer schedule Error", except);
                return null;
            }
        }

        public ScheduledFuture<?> scheduleAtTimestamp(Runnable r, long timestamp) {
            try {
                return stpe.schedule(r, timestamp - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            } catch (RejectedExecutionException except) {
                log.error("Timer scheduleAtTimestamp Error", except);
                return null;
            }
        }

        public void execute(Runnable r) {
            stpe.execute(r);
        }

        public ScheduledThreadPoolExecutor getExecutor() {
            return this.stpe;
        }

        public void purge() {
            stpe.purge();
        }
    }

    private class RunnableWrapper implements Runnable {
        private final Runnable _r;

        private RunnableWrapper(Runnable r) {
            this._r = r;
        }

        public final void run() {
            try {
                this._r.run();
            } catch (Exception var2) {
                TimerPool.log.error("Error while running RunnableWrapper:", var2);
                throw new RuntimeException(var2);
            }
        }
    }

    private class PurgeTask implements Runnable {
        private PurgeTask() {
        }

        public void run() {
            TimerPool.this.worldTimer.purge();
            TimerPool.this.mapTimer.purge();
            TimerPool.this.buffTimer.purge();
            TimerPool.this.etcTimer.purge();
        }
    }
}
