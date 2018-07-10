package com.msemu.world.service;

import com.msemu.core.startup.StartupComponent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/6/2.
 */
@StartupComponent("Service")
public class WorldEventService {

    private static final AtomicReference<WorldEventService> instance = new AtomicReference<>();

    public static WorldEventService getInstance() {
        WorldEventService value = instance.get();
        if (value == null) {
            synchronized (WorldEventService.instance) {
                value = instance.get();
                if (value == null) {
                    value = new WorldEventService();
                }
                instance.set(value);
            }
        }
        return value;
    }

    public WorldEventService() {

    }
}
