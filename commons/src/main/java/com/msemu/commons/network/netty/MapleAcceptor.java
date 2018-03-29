package com.msemu.commons.network.netty;

import com.msemu.commons.network.crypt.MapleCrypt;
import com.msemu.commons.network.netty.handler.MapleChannelHandler;
import com.msemu.core.configs.CoreConfig;
import com.msemu.core.configs.NetworkConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
    protected EventLoopGroup bossGroup = new NioEventLoopGroup();
    protected EventLoopGroup workerGroup = new NioEventLoopGroup();
    protected ServerBootstrap bootstrap = new ServerBootstrap();
    protected InetSocketAddress bindAddress;
    protected boolean shutdown;

    public MapleAcceptor(String host, int port) {
        this.bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(this);
        this.bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        this.bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    protected InetSocketAddress getBindAddress() {
        if (this.bindAddress == null) {
            if (NetworkConfig.HOST.isEmpty() || NetworkConfig.HOST.equals("*")) {
                this.bindAddress = new InetSocketAddress(NetworkConfig.PORT);
            }

            this.bindAddress = new InetSocketAddress(NetworkConfig.HOST, NetworkConfig.PORT);
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
        log.info("Waiting for connections on {}:{}", NetworkConfig.HOST, NetworkConfig.PORT);
        shutdown = false;
    }

    public void shutdown() throws IOException, InterruptedException {
        channelFuture.channel().close();
        channelFuture.channel().closeFuture();
        workerGroup.shutdownGracefully().sync();
        bossGroup.shutdownGracefully().sync();
        shutdown = true;
    }

    public abstract IClientFactory<TClient> getClientFactory();

    public abstract MapleChannelHandler<TClient> getChannelHandler();
}
