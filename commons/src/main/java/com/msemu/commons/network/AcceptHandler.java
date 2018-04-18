package com.msemu.commons.network;

import com.msemu.commons.network.filters.IAcceptFilter;
import com.msemu.commons.utils.SocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;

/**
 * Created by Weber on 2018/4/18.
 */
public class AcceptHandler<TClient extends Client<TClient>> implements CompletionHandler<AsynchronousSocketChannel, Void> {
    private static final Logger log = LoggerFactory.getLogger(AcceptHandler.class);
    private final NetworkThread<TClient> server;

    AcceptHandler(NetworkThread<TClient> server) {
        this.server = server;
    }

    public void submit() {
        if(!this.server.isShutdown()) {
            this.server.getServerSocketChannel().accept((Void)null, this);
        }

    }

    public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
        if(this.server.isShutdown()) {
            SocketUtils.closeAsyncChannelSilent(socketChannel);
        } else {
            this.submit();

            try {
                IAcceptFilter ex = this.server.getAcceptFilter();
                InetSocketAddress socketAddress = (InetSocketAddress)socketChannel.getRemoteAddress();
                if(ex.isAllowedAddress(socketAddress)) {
                    Connection<TClient> connection = new Connection<>(this.server);
                } else {
                    SocketUtils.closeAsyncChannelSilent(socketChannel);
                }
            } catch (Exception var6) {
                log.error("Exception thrown while processing accepted connection", var6);
                SocketUtils.closeAsyncChannelSilent(socketChannel);
            }

        }
    }

    public void failed(Throwable reason, Void attachment) {
        if(!this.server.isShutdown()) {
            if(!(reason instanceof ClosedChannelException)) {
                this.submit();
                log.error("Exception thrown while accepting connection", reason);
            }
        }
    }
}
