package com.msemu.core.network;

import com.msemu.commons.network.Connection;
import com.msemu.commons.network.IClientFactory;
import com.msemu.core.startup.StartupComponent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/29.
 */
@StartupComponent("Network")
public class LoginClientFactory implements IClientFactory<LoginClient> {

    private static final AtomicReference<LoginClientFactory> instance = new AtomicReference<>();

    public static LoginClientFactory getInstance() {
        LoginClientFactory value = instance.get();
        if (value == null) {
            synchronized (LoginClientFactory.instance) {
                value = instance.get();
                if (value == null) {
                    value = new LoginClientFactory();
                }
                instance.set(value);
            }
        }
        return value;
    }


    @Override
    public LoginClient createClient(Connection<LoginClient> connection) {
        return new LoginClient(connection);
    }
}
