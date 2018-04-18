package com.msemu.core.network;

import com.msemu.commons.network.handler.PropertiesPacketHandlerFactory;
import com.msemu.core.startup.StartupComponent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/19.
 */
@StartupComponent("Network")
public class LoginPacketFactory extends PropertiesPacketHandlerFactory<LoginClient> {

    private static final AtomicReference<Object> instance = new AtomicReference<>();

    public static LoginPacketFactory getInstance() {
        Object o = instance.get();
        if (o == null) {
            synchronized (LoginPacketFactory.instance) {
                o = instance.get();
                if (o == null) {
                    o = new LoginPacketFactory();
                }
                instance.set(o);
            }
        }
        return (LoginPacketFactory) ((o == LoginPacketFactory.instance) ? null : o);
    }

}
