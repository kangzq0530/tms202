package com.msemu.commons.network.netty.handler;

import com.msemu.commons.network.InHeader;
import com.msemu.commons.network.InPacket;
import com.msemu.commons.network.netty.NettyClient;

/**
 * Created by Weber on 2018/3/29.
 */
public interface IChannelHandler<TClient extends NettyClient<TClient>>{

    void handlePacket(InHeader opcode, InPacket packet, TClient client);

}
