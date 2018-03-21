package com.msemu.commons.network;

import com.msemu.commons.utils.SocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;

/**
 * Created by Weber on 2018/3/14.
 */
class AcceptHandler<TClient extends Client<TClient>> implements CompletionHandler<AsynchronousSocketChannel, Void> {

    private static final Logger log = LoggerFactory.getLogger(AcceptHandler.class);
    private final NetworkThread<TClient> server;

    AcceptHandler(NetworkThread<TClient> server) {
        this.server = server;
    }

    public void submit() {
        if (!this.server.isShutdown()) {
            this.server.getServerSocketChannel().accept();
        }

    }

    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
        if (this.server.isShutdown()) {
            SocketUtils.closeAsyncChannelSilent(socketChannel);
        } else {
            this.submit();

            try {
                IAcceptFilter ex = this.server.getAcceptFilter();
                InetSocketAddress socketAddress = (InetSocketAddress) socketChannel.getRemoteAddress();
                if (ex.isAllowedAddress(socketAddress)) {
                    final Connection<TClient> connection = new Connection<>(this.server);
                } else {
                    SocketUtils.closeAsyncChannelSilent(socketChannel);
                }
            } catch (Exception ex) {
                log.error("Exception thrown while processing accepted connection", ex);
                SocketUtils.closeAsyncChannelSilent(socketChannel);
            }

        }
    }

    @Override
    public void failed(Throwable reason, Void attachment) {
        if (!this.server.isShutdown()) {
            if (!(reason instanceof ClosedChannelException)) {
                this.submit();
                log.error("Exception thrown while accepting connection", reason);
            }
        }
    }
}
