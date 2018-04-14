package com.msemu.login.network.netty;

import com.msemu.commons.network.netty.IClientFactory;
import com.msemu.commons.network.netty.MapleAcceptor;
import com.msemu.commons.network.netty.handler.MapleChannelHandler;
import com.msemu.core.configs.NetworkConfig;
import com.msemu.core.startup.StartupComponent;
import com.msemu.login.client.LoginClient;
import com.msemu.login.network.packets.LoginServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/29.
 */
@StartupComponent("Network")
public class LoginAcceptor extends MapleAcceptor<LoginClient> {

    private static final Logger log = LoggerFactory.getLogger("LoginServer");
    private static final AtomicReference<LoginAcceptor> instance = new AtomicReference<>();

    public static LoginAcceptor getInstance() {
        LoginAcceptor value = instance.get();
        if (value == null) {
            synchronized (LoginAcceptor.instance) {
                value = instance.get();
                if (value == null) {
                    value = new LoginAcceptor();
                }
                instance.set(value);
            }
        }
        return value;
    }

    public LoginAcceptor() {
        super(NetworkConfig.HOST, NetworkConfig.PORT);
    }

    @Override
    public void startup() throws IOException, InterruptedException {
        super.startup();

    }

    @Override
    public IClientFactory<LoginClient> getClientFactory() {
        return LoginClientFactory.getInstance();
    }

    @Override
    public MapleChannelHandler<LoginClient> getChannelHandler() {
        return new LoginServerHandler();
    }
}
