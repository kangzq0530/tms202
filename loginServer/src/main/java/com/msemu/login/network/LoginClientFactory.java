package com.msemu.login.network;

import com.msemu.commons.network.netty.IClientFactory;
import com.msemu.commons.utils.Rand;
import com.msemu.core.startup.StartupComponent;
import com.msemu.login.client.LoginClient;
import com.msemu.login.service.AutoBanService;
import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/29.
 */
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
    public LoginClient createClient(Channel channel) {
        byte[] siv = new byte[4];
        byte[] riv = new byte[4];
        Rand.nextBytes(siv);
        Rand.nextBytes(riv);
        return new LoginClient(channel, siv, riv);
    }
}
