package com.msemu.login.client;

import com.msemu.commons.network.netty.NettyClient;
import com.msemu.login.packets.send.LoginPacket;
import io.netty.channel.Channel;

/**
 * Created by Weber on 2018/3/29.
 */
public class LoginClient extends NettyClient<LoginClient> {



    public LoginClient(Channel c, byte[] alpha, byte[] delta) {
        super(c, alpha, delta);
    }

    @Override
    public void onInit() {
        write(LoginPacket.sendConnect(this));
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {

    }
}
