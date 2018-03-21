package com.msemu.core.network;

import com.msemu.commons.network.Connection;
import com.msemu.commons.network.IClientFactory;
import com.msemu.core.startup.StartupComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/15.
 */
@StartupComponent("Network")
public class LoginClientFactory implements IClientFactory<LoginClient> {
    private static final Logger log;
    private static final AtomicReference<LoginClientFactory> instance;

    static {
        log = LoggerFactory.getLogger(LoggerFactory.class);
        instance = new AtomicReference<>();
    }

    public LoginClient createClient(final Connection<LoginClient> connection) {
        return new LoginClient(connection);
    }

    public static LoginClientFactory getInstance() {
        LoginClientFactory value = instance.get();
        if (value == null) {
            synchronized (LoginClientFactory.instance) {
                value = LoginClientFactory.instance.get();
                if (value == null) {
                    value = new LoginClientFactory();
                }
                instance.set(value);
            }
        }
        return value;
    }


}

