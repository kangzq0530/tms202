package com.msemu.commons.thread;

import com.msemu.core.configs.ThreadsConfig;
import com.msemu.core.startup.StartupComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/14.
 */
@StartupComponent("Event")
public class EventManager {
    private static final Logger log = LoggerFactory.getLogger(EventManager.class);
    private static final AtomicReference<EventManager> instance = new AtomicReference<>();
    private final ScheduledExecutorService scheduler;

    public EventManager() {
        scheduler = Executors.newScheduledThreadPool(ThreadsConfig.EVENT_POOL_SIZE);
        log.info("EventManager initialized");
    }

    public static EventManager getInstance() {
        EventManager value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new EventManager();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    /**
     * Adds and returns an event that executes after a given delay.
     *
     * @param callable The method that should be called
     * @param delay    The delay (in ms) after which the call should start
     * @param <V>      Return type of the given callable
     * @return The created event (ScheduledFuture)
     */
    public <V> ScheduledFuture<V> addEvent(Callable<V> callable, long delay) {
        return scheduler.schedule(callable, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Adds and returns an event that executes after a given delay.
     *
     * @param callable The method that should be called
     * @param delay    The delay after which the call should start
     * @param timeUnit The time unit of the delay
     * @param <V>      The return type of the given callable
     * @return The created event (ScheduledFuture)
     */
    public <V> ScheduledFuture<V> addEvent(Callable<V> callable, long delay, TimeUnit timeUnit) {
        return scheduler.schedule(callable, delay, timeUnit);
    }

    /**
     * Adds and returns an event that executes after a given initial delay, and then after every delay.
     * See https://stackoverflow.com/questions/24649842/scheduleatfixedrate-vs-schedulewithfixeddelay for difference
     * between this method and addFixedDelayEvent.
     *
     * @param runnable     The method that should be run
     * @param initialDelay The time that it should take before the first execution should start
     * @param delay        The time it should (in ms) take between the start of execution n and execution n+1
     * @return The created event (ScheduledFuture)
     */
    public ScheduledFuture addFixedRateEvent(Runnable runnable, long initialDelay, long delay) {
        return scheduler.scheduleAtFixedRate(runnable, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Adds and returns an event that executes after a given initial delay, and then after every delay.
     * See https://stackoverflow.com/questions/24649842/scheduleatfixedrate-vs-schedulewithfixeddelay for difference
     * between this method and addFixedDelayEvent.
     *
     * @param runnable     The method that should be run
     * @param initialDelay The time that it should take before the first execution should start
     * @param delay        The time it should (in ms) take between the start of execution n and execution n+1
     * @param executes     The amount of times the
     * @return The created event (ScheduledFuture)
     */
    public ScheduledFuture addFixedRateEvent(Runnable runnable, long initialDelay, long delay, int executes) {
        ScheduledFuture sf = scheduler.scheduleAtFixedRate(runnable, initialDelay, delay, TimeUnit.MILLISECONDS);
        addEvent(() -> sf.cancel(false), 10 + initialDelay + delay * executes);
        return sf;
    }

    /**
     * Adds and returns an event that executes after a given initial delay, and then after every delay.
     * See https://stackoverflow.com/questions/24649842/scheduleatfixedrate-vs-schedulewithfixeddelay for difference
     * between this method and addFixedDelayEvent.
     *
     * @param runnable     The method that should be run
     * @param initialDelay The time that it should take before the first execution should start
     * @param delay        The time it should take between the start of execution n and execution n+1
     * @param timeUnit     The time unit of the delays
     * @return The created event (ScheduledFuture)
     */
    public ScheduledFuture addFixedRateEvent(Runnable runnable, long initialDelay, long delay, TimeUnit timeUnit) {
        return scheduler.scheduleAtFixedRate(runnable, initialDelay, delay, timeUnit);
    }

    /**
     * Adds and returns an event that executes after a given initial delay, and then after every delay after the task has finished.
     * See https://stackoverflow.com/questions/24649842/scheduleatfixedrate-vs-schedulewithfixeddelay for difference
     * between this method and addFixedDelayEvent.
     *
     * @param runnable     The method that should be run
     * @param initialDelay The time that it should take before the first execution should start
     * @param delay        The time it should (in ms) take between the start of execution n and execution n+1
     * @return The created event (ScheduledFuture)
     */
    public ScheduledFuture addFixedDelayEvent(Runnable runnable, long initialDelay, long delay) {
        return scheduler.scheduleWithFixedDelay(runnable, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Adds and returns an event that executes after a given initial delay, and then after every delay.
     * See https://stackoverflow.com/questions/24649842/scheduleatfixedrate-vs-schedulewithfixeddelay for difference
     * between this method and addFixedDelayEvent.
     *
     * @param runnable     The method that should be run
     * @param initialDelay The time that it should take before the first execution should start
     * @param delay        The time it should take between the start of execution n and execution n+1
     * @param timeUnit     The time unit of the delay
     * @return The created event (ScheduledFuture)
     */
    public ScheduledFuture addFixedDelayEvent(Runnable runnable, long initialDelay, long delay, TimeUnit timeUnit) {
        return scheduler.scheduleWithFixedDelay(runnable, initialDelay, delay, timeUnit);
    }

    /**
     * Adds and returns an event that executes after a given delay.
     *
     * @param runnable The method that should be run
     * @param delay    The delay (in ms) after which the call should start
     * @return The created event (ScheduledFuture)
     */
    public ScheduledFuture addEvent(Runnable runnable, long delay) {
        return scheduler.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Adds and returns an event that executes after a given delay.
     *
     * @param runnable The method that should be run
     * @param delay    The delay after which the call should start
     * @param timeUnit The time unit of the delay
     * @return The created event (ScheduledFuture)
     */
    public ScheduledFuture addEvent(Runnable runnable, long delay, TimeUnit timeUnit) {
        return scheduler.schedule(runnable, delay, timeUnit);
    }
}
