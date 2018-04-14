package com.msemu.login.client;

import com.msemu.commons.network.netty.NettyClient;
import com.msemu.login.network.packets.send.login.ConnectToClient;
import io.netty.channel.Channel;

/**
 * Created by Weber on 2018/3/29.
 */
public class LoginClient extends NettyClient<LoginClient> {


    private Account accountSchema;

    private int world;

    private int channel;

    private boolean authorized = false;

    public LoginClient(Channel c, byte[] alpha, byte[] delta) {
        super(c, alpha, delta);
    }

    @Override
    public void onInit() {
        write(new ConnectToClient(this));
    }

    @Override
    public void onOpen() {
        return;
    }

    @Override
    public void onClose() {
        ch.attr(NettyClient.CLIENT_KEY).set(null);
        ch.attr(NettyClient.CRYPTO_KEY).set(null);
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public Account getAccount() {
        return accountSchema;
    }

    public void setAccountSchema(Account accountSchema) {
        this.accountSchema = accountSchema;
    }

    public int getWorld() {
        return world;
    }

    public void setWorld(int world) {
        this.world = world;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
