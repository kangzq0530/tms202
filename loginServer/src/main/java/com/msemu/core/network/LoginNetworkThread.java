package com.msemu.core.network;

import com.msemu.commons.network.IAcceptFilter;
import com.msemu.commons.network.IClientFactory;
import com.msemu.commons.network.NetworkThread;
import com.msemu.commons.network.handler.AbstractPacketHandlerFactory;
import com.msemu.core.startup.StartupComponent;
import com.msemu.login.service.FirewallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/15.
 */
@StartupComponent("Network")
public class LoginNetworkThread extends NetworkThread<LoginClient> {
    private static final Logger log;
    private static final AtomicReference<LoginNetworkThread> instance;

    static {
        log = LoggerFactory.getLogger((Class) LoginNetworkThread.class);
        instance = new AtomicReference<>();
    }

    public IClientFactory<LoginClient> getClientFactory() {
        return LoginClientFactory.getInstance();
    }

    public AbstractPacketHandlerFactory<LoginClient> getPacketHandler() {
        return LoginPacketFactory.getInstance();
    }

    public IAcceptFilter getAcceptFilter() {
        return FirewallService.getFirewallFilter();
    }

    public static LoginNetworkThread getInstance() {
        LoginNetworkThread value = instance.get();
        if (value == null) {
            synchronized (LoginNetworkThread.instance) {
                value = instance.get();
                if (value == null) {
                    value = new LoginNetworkThread();
                }
                instance.set(value);
            }
        }
        return value;
    }
}
