package com.msemu.core.network;

import com.msemu.commons.network.IClientFactory;
import com.msemu.commons.network.NetworkThread;
import com.msemu.commons.network.handler.AbstractPacketHandlerFactory;
import com.msemu.world.client.GameClient;

/**
 * Created by Weber on 2018/4/18.
 */
public class ChannelNetworkThread extends NetworkThread<GameClient> {

    protected ChannelNetworkThread(String host, int port) {
        super(host, port);
    }

    @Override
    public IClientFactory<GameClient> getClientFactory() {
        return GameClientFactory.getInstance();
    }

    @Override
    public AbstractPacketHandlerFactory<GameClient> getPacketHandler() {
        return WorldPacketFactory.getInstance();
    }
}
