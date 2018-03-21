package com.msemu.login.service;

import com.msemu.core.startup.StartupComponent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/14.
 */

@StartupComponent("Service")
public class AutoBanService {
    private static final AtomicReference<AutoBanService> instance = new AtomicReference<>();

    public AutoBanService() {

    }

    public static AutoBanService getInstance() {
        AutoBanService value = instance.get();
        if (value == null) {
            synchronized (AutoBanService.instance) {
                value = instance.get();
                if (value == null) {
                    value = new AutoBanService();
                }
                instance.set(value);
            }
        }
        return value;
    }
}
