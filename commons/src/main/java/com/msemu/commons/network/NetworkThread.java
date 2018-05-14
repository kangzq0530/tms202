package com.msemu.commons.network;

import com.msemu.commons.network.filters.AlwaysAcceptFilter;
import com.msemu.commons.network.filters.IAcceptFilter;
import com.msemu.commons.network.handler.AbstractPacketHandlerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Getter;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * Created by Weber on 2018/4/18.
 */
public abstract class NetworkThread<TClient extends Client<TClient>>{
    private static final Logger log = LoggerFactory.getLogger(NetworkThread.class);
    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private final EventLoopGroup mainLoopGroup;
    private final EventLoopGroup workerLoopGroup;
    private ChannelFuture future;
    private InetSocketAddress bindAddress;
    private Pair<SocketOption, Object>[] clientSocketOptions;
    private Pair<SocketOption, Object>[] serverSocketOptions;
    private final AcceptHandler<TClient> acceptHandler;
    @Getter
    private final IAcceptFilter acceptFilter;
    private AsynchronousChannelGroup channelGroup;
    @Getter
    protected AsynchronousServerSocketChannel serverSocketChannel;
    @Getter
    private volatile boolean isShutdown;
    private String host;
    private int port;

    protected NetworkThread(String host, int port) {
        this.host = host;
        this.port = port;
        this.mainLoopGroup = SystemUtils.IS_OS_WINDOWS ?
                new NioEventLoopGroup(1) : new EpollEventLoopGroup(1);
        this.workerLoopGroup = SystemUtils.IS_OS_WINDOWS ?
                new NioEventLoopGroup(1) : new EpollEventLoopGroup(4);

        this.bootstrap.group(this.mainLoopGroup, this.workerLoopGroup)
                .channel(SystemUtils.IS_OS_WINDOWS ?
                        NioServerSocketChannel.class : EpollServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.config().setPerformancePreferences(0, 2, 1);
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast("readTimeoutHandler",new IdleStateHandler(10, 0, 0, TimeUnit.MINUTES));
                        pipeline.addLast("packetDecoder", new PacketDecoder());
                        pipeline.addLast("packetEncoder", new PacketEncoder());
                        pipeline.addLast(new Connection<>(NetworkThread.this));
                    }
                });
        this.acceptFilter = new AlwaysAcceptFilter();
        this.isShutdown = false;
        this.acceptHandler = new AcceptHandler<>(this);
        this.bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        this.bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    public abstract IClientFactory<TClient> getClientFactory();

    public abstract AbstractPacketHandlerFactory<TClient> getPacketHandler();

    private InetSocketAddress getBindAddress() {
        if(this.bindAddress == null) {
            if(host.isEmpty() || host.equals("*")) {
                this.bindAddress = new InetSocketAddress(port);
            }
            this.bindAddress = new InetSocketAddress(host, port);
        }
        return this.bindAddress;
    }

    public void startup() throws IOException, InterruptedException {
        this.future = this.bootstrap.bind(host, port).sync();
        this.isShutdown = false;
        log.info("Waiting for connections on {}:{}", host, port);
    }

    public void shutdown() throws IOException, InterruptedException {
        this.future.channel().close();
        this.future.channel().closeFuture().sync();
        this.workerLoopGroup.shutdownGracefully().sync();
        this.mainLoopGroup.shutdownGracefully().sync();
        this.isShutdown = true;
    }

    @Override
    public String toString() {
        try {
            InetSocketAddress address = (InetSocketAddress)this.getServerSocketChannel().getLocalAddress();
            return "{NetworkThread listening at " + address.getHostName() + ":" + address.getPort() + "}";
        } catch (Exception var2) {
            return "{NetworkThread}";
        }
    }
}
