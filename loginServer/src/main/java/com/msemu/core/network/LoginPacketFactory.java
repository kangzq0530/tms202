package com.msemu.core.network;

import com.msemu.commons.network.Connection;
import com.msemu.commons.network.handler.PropertyPacketHandlerFactory;
import com.msemu.core.startup.StartupComponent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/15.
 */
@StartupComponent("Network")
public class LoginPacketFactory extends PropertyPacketHandlerFactory<LoginClient> {
    private static final AtomicReference<LoginPacketFactory> instance = new AtomicReference<>();

    public LoginPacketFactory(String fileName) {
        super();
    }

    public static LoginPacketFactory getInstance() {
        LoginPacketFactory value = instance.get();
        if (value == null) {
            synchronized (LoginPacketFactory.instance) {
                value = instance.get();
                if (value == null) {
                    value = new LoginPacketFactory("");
                }
                instance.set(value);
            }
        }
        return value;
    }
}
