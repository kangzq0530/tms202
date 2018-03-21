package com.msemu.commons.network;

import com.msemu.commons.network.handler.AbstractPacketHandlerFactory;
import com.msemu.commons.thread.TimerPool;
import com.msemu.commons.utils.SocketUtils;
import com.msemu.core.configs.NetworkConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.ByteOrder;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executors;

/**
 * Created by Weber on 2018/3/14.
 */
public abstract class NetworkThread<TClient extends Client<TClient>> {
    private static final Logger log = LoggerFactory.getLogger(NetworkThread.class);
    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private final EventLoopGroup mainLoopGroup;
    private final EventLoopGroup workerLoopGroup;
    private final AcceptHandler acceptHandler;
    private final AlwaysAcceptFilter ACCEPT_FILTER;
    protected AsynchronousServerSocketChannel serverSocketChannel;
    private ChannelFuture future;
    private InetSocketAddress bindAddress;
    private Pair<SocketOption, Object>[] clientSocketOptions;
    private Pair<SocketOption, Object>[] serverSocketOptions;
    private AsynchronousChannelGroup channelGroup;
    private volatile boolean isShutdown;

    public NetworkThread() {
        this.mainLoopGroup = SystemUtils.IS_OS_WINDOWS ? new NioEventLoopGroup(1) : new EpollEventLoopGroup(1);
        this.workerLoopGroup = SystemUtils.IS_OS_WINDOWS ? new NioEventLoopGroup(16) : new EpollEventLoopGroup(16);
        this.bootstrap.group(this.mainLoopGroup, this.workerLoopGroup)
                .channel(SystemUtils.IS_OS_WINDOWS ? NioServerSocketChannel.class : EpollServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.config().setPerformancePreferences(0, 2, 1);
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(240));
                        //pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, Short.MAX_VALUE, 0, 2, 2, 0, true));
                        //pipeline.addLast("frameEncoder", new LengthFieldPrepender(ByteOrder.LITTLE_ENDIAN, 4, 0, false));
                        pipeline.addLast("bytesEncoder", new ByteArrayEncoder());
                        pipeline.addLast(new Connection<>(NetworkThread.this));
                    }
                })
                .option(ChannelOption.SO_RCVBUF, NetworkConfig.RECV_BUFFER_SIZE)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true);
        this.ACCEPT_FILTER = new AlwaysAcceptFilter();
        this.isShutdown = false;
        this.acceptHandler = new AcceptHandler<>(this);
    }

    public final NetworkThread<TClient> getThis() {
        return this;
    }

    public AsynchronousServerSocketChannel getServerSocketChannel() {
        return this.serverSocketChannel;
    }

    private InetSocketAddress getBindAddress() {
        if (this.bindAddress == null) {
            if (NetworkConfig.HOST.isEmpty() || NetworkConfig.HOST.equals("*")) {
                this.bindAddress = new InetSocketAddress(NetworkConfig.PORT);
            }

            this.bindAddress = new InetSocketAddress(NetworkConfig.HOST, NetworkConfig.PORT);
        }

        return this.bindAddress;
    }

    Pair<SocketOption, Object>[] getClientSocketOptions() {
        if (this.clientSocketOptions == null) {
            this.clientSocketOptions = SocketUtils.parseSocketOptions(NetworkConfig.CLIENT_SOCKET_OPTIONS);
        }

        return this.clientSocketOptions;
    }

    private Pair<SocketOption, Object>[] getServerSocketOptions() {
        if (this.serverSocketOptions == null) {
            this.serverSocketOptions = SocketUtils.parseSocketOptions(NetworkConfig.SERVER_SOCKET_OPTIONS);
        }

        return this.serverSocketOptions;
    }

    private AsynchronousChannelGroup getChannelGroup() {
        if (this.channelGroup == null) {
            try {
                switch (NetworkConfig.IO_EXECUTION_MODE) {
                    case FIXED:
                        this.channelGroup = AsynchronousChannelGroup.withFixedThreadPool(NetworkConfig.IO_EXECUTION_THREAD_NUM, Executors.defaultThreadFactory());
                        break;
                    case POOLED:
                        this.channelGroup = AsynchronousChannelGroup.withThreadPool(TimerPool.getInstance().getNetworkTimer().getExecutor());
                }
            } catch (Exception var2) {
                log.error("Error while creating AsynchronousChannelGroup", var2);
            }
        }

        return this.channelGroup;
    }


    public IAcceptFilter getAcceptFilter() {
        return this.ACCEPT_FILTER;
    }

    public void startup() throws IOException, InterruptedException {
        this.future = this.bootstrap.bind(NetworkConfig.HOST, NetworkConfig.PORT).sync();
        this.isShutdown = false;
        log.info("伺服器 {} : {} 等待連接", NetworkConfig.HOST, NetworkConfig.PORT);
    }

    public void shutdown() throws IOException, InterruptedException {
        this.future.channel().close();
        this.future.channel().closeFuture().sync();
        this.workerLoopGroup.shutdownGracefully().sync();
        this.mainLoopGroup.shutdownGracefully().sync();
        this.isShutdown = true;
    }


    public boolean isShutdown() {
        return this.isShutdown;
    }

    public abstract IClientFactory<TClient> getClientFactory();

    public abstract AbstractPacketHandlerFactory<TClient> getPacketHandler();

    @Override
    public String toString() {
        try {
            InetSocketAddress var1 = (InetSocketAddress) this.getServerSocketChannel().getLocalAddress();
            return "{NetworkThread listening at " + var1.getHostName() + ":" + var1.getPort() + "}";
        } catch (Exception var2) {
            return "{NetworkThread}";
        }
    }

}
