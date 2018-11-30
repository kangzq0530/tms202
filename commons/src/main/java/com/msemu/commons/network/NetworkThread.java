/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
public abstract class NetworkThread<TClient extends Client<TClient>> {
    private static final Logger log = LoggerFactory.getLogger(NetworkThread.class);
    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private final EventLoopGroup mainLoopGroup;
    private final EventLoopGroup workerLoopGroup;
    private final AcceptHandler<TClient> acceptHandler;
    @Getter
    private final IAcceptFilter acceptFilter;
    @Getter
    protected AsynchronousServerSocketChannel serverSocketChannel;
    private ChannelFuture future;
    private InetSocketAddress bindAddress;
    private Pair<SocketOption, Object>[] clientSocketOptions;
    private Pair<SocketOption, Object>[] serverSocketOptions;
    private AsynchronousChannelGroup channelGroup;
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
                        pipeline.addLast("readTimeoutHandler", new IdleStateHandler(1, 0, 0, TimeUnit.MINUTES));
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
        if (this.bindAddress == null) {
            if (host.isEmpty() || host.equals("*")) {
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
            InetSocketAddress address = (InetSocketAddress) this.getServerSocketChannel().getLocalAddress();
            return "{NetworkThread listening at " + address.getHostName() + ":" + address.getPort() + "}";
        } catch (Exception var2) {
            return "{NetworkThread}";
        }
    }
}
