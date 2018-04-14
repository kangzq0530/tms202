package com.msemu.world.network.netty;

import com.msemu.commons.network.netty.IClientFactory;
import com.msemu.commons.network.netty.MapleAcceptor;
import com.msemu.commons.network.netty.handler.MapleChannelHandler;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.GameClient;

/**
 * Created by Weber on 2018/3/30.
 */

public class ChannelAcceptor extends MapleAcceptor<GameClient> {

    public ChannelAcceptor(String host, int port) {
        super(host, port);
    }

    @Override
    public IClientFactory<GameClient> getClientFactory() {
        return GameClientFactory.getInstance();
    }

    @Override
    public MapleChannelHandler<GameClient> getChannelHandler() {
        return null;
    }
}
