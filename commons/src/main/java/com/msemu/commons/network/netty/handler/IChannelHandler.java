package com.msemu.commons.network.netty.handler;

import com.msemu.commons.enums.InHeader;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.network.netty.NettyClient;

/**
 * Created by Weber on 2018/3/29.
 */
public interface IChannelHandler<TClient extends NettyClient<TClient>>{

    void handlePacket(InHeader opcode, InPacket packet, TClient client);

}
