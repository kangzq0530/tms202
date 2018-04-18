package com.msemu.login.service;

import com.msemu.core.startup.StartupComponent;
import com.msemu.login.modules.FirewallFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/15.
 */
@StartupComponent("Service")
public class FirewallService {
    private static final Logger log = LoggerFactory.getLogger((Class) FirewallService.class);
    private static final AtomicReference<FirewallService> instance = new AtomicReference<FirewallService>();
    private static FirewallFilter firewallFilter = new FirewallFilter();

    public static FirewallService getInstance() {
        FirewallService value = instance.get();
        if (value == null) {
            synchronized (FirewallService.instance) {
                value = instance.get();
                if (value == null) {
                    value = new FirewallService();
                }
                instance.set(value);
            }
        }
        return value;
    }

    public static FirewallFilter getFirewallFilter() {
        return FirewallService.firewallFilter;
    }

}