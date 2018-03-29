package com.msemu.commons.network.netty;

import io.netty.channel.Channel;

/**
 * Created by Weber on 2018/3/29.
 */
public interface IClientFactory<TClient extends NettyClient<TClient>> {
    TClient createClient(Channel channel);
}
