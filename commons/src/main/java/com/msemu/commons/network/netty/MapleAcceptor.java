package com.msemu.commons.network.netty;

import com.msemu.commons.network.crypt.MapleCrypt;
import com.msemu.commons.network.netty.handler.MapleChannelHandler;
import com.msemu.core.configs.CoreConfig;
import com.msemu.core.configs.ThreadsConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;

/**
 * Created by Weber on 2018/3/29.
 */
public abstract class MapleAcceptor<TClient extends NettyClient<TClient>> extends ChannelInitializer<SocketChannel> {

    protected static final Logger log = LoggerFactory.getLogger("Network");

    protected ChannelFuture channelFuture;
    protected final EventLoopGroup bossGroup ;
    protected final EventLoopGroup workerGroup ;
    protected final ServerBootstrap bootstrap = new ServerBootstrap();
    protected InetSocketAddress bindAddress;
    protected boolean shutdown;
    protected String host;
    protected int port;

    public MapleAcceptor(String host, int port) {
        this.bossGroup = SystemUtils.IS_OS_WINDOWS ?
                new NioEventLoopGroup(1) :new  EpollEventLoopGroup(1);
        this.workerGroup = SystemUtils.IS_OS_WINDOWS ?
                new NioEventLoopGroup(16) : new EpollEventLoopGroup(16);
        this.bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(this);
        this.bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        this.bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        this.host= host;
        this.port = port;
    }

    protected InetSocketAddress getBindAddress() {
        if (this.bindAddress == null) {
            if (getHost().isEmpty() || getHost().equals("*")) {
                this.bindAddress = new InetSocketAddress(getPort());
            }

            this.bindAddress = new InetSocketAddress(getHost(), getPort());
        }
        return this.bindAddress;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline().addLast(new PacketDecoder(), getChannelHandler(), new PacketEncoder());
        TClient client = getClientFactory().createClient(channel);
        client.onInit();
        channel.attr(NettyClient.CLIENT_KEY).set(client);
        channel.attr(NettyClient.CRYPTO_KEY).set(new MapleCrypt(CoreConfig.GAME_SERVICE_TYPE, (short)CoreConfig.GAME_VERSION));
    }


    public void startup() throws IOException, InterruptedException {
        channelFuture = bootstrap.bind(this.getBindAddress()).sync();
        log.info("Waiting for connections on {}:{}", getHost(), getPort());
        shutdown = false;
    }

    public void shutdown() throws IOException, InterruptedException {
        channelFuture.channel().close();
        channelFuture.channel().closeFuture();
        workerGroup.shutdownGracefully().sync();
        bossGroup.shutdownGracefully().sync();
        shutdown = true;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public abstract IClientFactory<TClient> getClientFactory();

    public abstract MapleChannelHandler<TClient> getChannelHandler();
}
