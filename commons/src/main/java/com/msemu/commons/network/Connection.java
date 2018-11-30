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

import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.enums.InHeader;
import com.msemu.commons.network.crypt.ICipher;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.HexUtils;
import com.msemu.core.configs.CoreConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/18.
 */
public class Connection<TClient extends Client<TClient>> extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(Connection.class);

    @Getter
    @Setter
    private ICipher sendCipher;

    @Getter
    @Setter
    private ICipher recvCipher;

    @Getter
    private TClient client;
    private Channel channel;

    @Getter
    private NetworkThread<TClient> server;
    private AtomicReference<EConnectionState> connectionState;

    public Connection(NetworkThread<TClient> server) {
        this.server = server;
        connectionState = new AtomicReference<>(EConnectionState.OPEN);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        this.client = this.server.getClientFactory().createClient(this);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.channel = ctx.channel();
        this.channel.attr(Client.CLIENT_KEY).set(this.client);
        this.client.onOpen();

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.channel.attr(Client.CLIENT_KEY).set(null);
        this.client.onClose();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE)
                this.client.onIdle();
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        this.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) {
        if (object instanceof byte[]) {

            byte[] data = (byte[]) object;
            ByteBuf buff = Unpooled.copiedBuffer(data);

            try {
                short opcode = buff.readShortLE();

                InPacket<TClient> inPacket = this.getServer().getPacketHandler().handleClientPacket(opcode, getClient());

                if (inPacket != null) {
                    if(!client.isConnected())
                        return;
                    inPacket.setClient(client);
                    inPacket.setByteBuf(buff);
                    InHeader header = InHeader.getInHeaderByOp(opcode);
                    if (CoreConfig.SHOW_PACKET && (header == null || !header.ignoreDebug())) {
                        log.warn(String.format("[In]\t| %s, %d/0x%s\t| Length: %d\n\t[All]\t%s\n\t[ASCII]\t%s", InHeader.getInHeaderByOp(opcode), opcode, Integer.toHexString(opcode).toUpperCase(), inPacket.getLength(), inPacket, HexUtils.toAscii(inPacket.getData())));
                    }
                    try {
                        inPacket.read();
                        inPacket.runImpl();
                    } catch (Exception e) {
                        log.error(String.format("Created packet [%s] has not been read.\n\t[All]\t%s\n\t[Now]\t%s\n", inPacket.getClass().getSimpleName(),
                                inPacket.toString(), inPacket.toString(false)), inPacket.getClass().getSimpleName(), e);
                        this.close();
                    }
                } else {
                    InHeader header = InHeader.getInHeaderByOp(opcode);
                    if (header != null && !header.ignoreDebug())
                        log.error(String.format("[In][Unknown][state=%s]\t| %s, %d/0x%s\t| Length: %d\n\t[All]\t%s\n\t[ASCII]\t%s", client.getState(), InHeader.getInHeaderByOp(opcode), opcode, Integer.toHexString(opcode).toUpperCase(), buff.array().length, HexUtils.readableByteArray(Arrays.copyOfRange(buff.array(), 2, buff.array().length)), HexUtils.toAscii(buff.array())));
                }

            } catch (Exception e) {
                log.error("PacketReadError", e);
                this.close();
            }


        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable except) {
        ctx.close();
    }

    public void write(OutPacket<TClient> packet) {
        this.channel.writeAndFlush(packet);
    }

    public InetSocketAddress getSocketAddress() {
        return this.channel != null ? (InetSocketAddress) this.channel.remoteAddress() : null;
    }

    public boolean isClosedOrPending() {
        switch (connectionState.get()) {
            case CLOSED:
            case PENDING_CLOSE:
                return true;
            default:
                return false;
        }
    }

    protected synchronized void close() {
        if (connectionState.compareAndSet(Connection.EConnectionState.OPEN, EConnectionState.CLOSED)
                || connectionState.compareAndSet(EConnectionState.PENDING_CLOSE, EConnectionState.CLOSED)
                && this.client != null) {
            this.client.close();
        }
        if (this.channel != null) {
            this.channel.close();
        }
    }


    private enum EConnectionState {
        OPEN,
        PENDING_CLOSE,
        CLOSED;
    }
}
