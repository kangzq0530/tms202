package com.msemu.core.network;

import com.msemu.commons.network.IClientFactory;
import com.msemu.commons.network.NetworkThread;
import com.msemu.commons.network.handler.AbstractPacketHandlerFactory;
import com.msemu.core.configs.NetworkConfig;
import com.msemu.core.startup.StartupComponent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/19.
 */
@StartupComponent("Network")
public class LoginNetworkThread extends NetworkThread<LoginClient> {

    private static final AtomicReference<Object> instance = new AtomicReference<>();

    protected LoginNetworkThread() {
        super(NetworkConfig.HOST, NetworkConfig.PORT);
    }

    @Override
    public IClientFactory<LoginClient> getClientFactory() {
        return LoginClientFactory.getInstance();
    }

    @Override
    public AbstractPacketHandlerFactory<LoginClient> getPacketHandler() {
        return LoginPacketFactory.getInstance();
    }

    public static LoginNetworkThread getInstance() {
        Object value = instance.get();
        if (value == null) {
            synchronized (LoginNetworkThread.instance) {
                value = instance.get();
                if (value == null) {
                    value = new LoginNetworkThread();
                }
                instance.set(value);
            }
        }
        return (LoginNetworkThread) ((value == LoginNetworkThread.instance) ? null : value);
    }
}
