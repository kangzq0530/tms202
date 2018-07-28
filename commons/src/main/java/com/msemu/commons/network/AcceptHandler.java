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
        if (!this.server.isShutdown()) {
            this.server.getServerSocketChannel().accept((Void) null, this);
        }

    }

    public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
        if (this.server.isShutdown()) {
            SocketUtils.closeAsyncChannelSilent(socketChannel);
        } else {
            this.submit();

            try {
                IAcceptFilter ex = this.server.getAcceptFilter();
                InetSocketAddress socketAddress = (InetSocketAddress) socketChannel.getRemoteAddress();
                if (ex.isAllowedAddress(socketAddress)) {
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
        if (!this.server.isShutdown()) {
            if (!(reason instanceof ClosedChannelException)) {
                this.submit();
                log.error("Exception thrown while accepting connection", reason);
            }
        }
    }
}
