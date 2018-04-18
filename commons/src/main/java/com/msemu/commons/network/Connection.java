package com.msemu.commons.network;

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
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/18.
 */
public class Connection<TClient extends Client<TClient>> extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(Connection.class);

    @Getter
    @Setter
    private ICipher cipher;

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
                    inPacket.setClient(client);
                    inPacket.setByteBuf(buff);
                    if (CoreConfig.SHOW_PACKET) {
                        log.warn(String.format("[In]\t| %s, %d/0x%s\n\t[All]\t%s\n\t[ASCII]\t%s", InHeader.getInHeaderByOp(opcode), opcode, Integer.toHexString(opcode).toUpperCase(), inPacket, HexUtils.toAscii(inPacket.getData())));
                    }
                    try {
                        inPacket.read();
                        inPacket.runImpl();
                    } catch (Exception e) {
                        log.error("Created packet [{}] has not been read.", inPacket.getClass().getSimpleName(), e);
                        this.close();
                    }
                } else {
                    log.error(String.format("[In][Unknown]\t| %s, %d/0x%s\n\t[All]\t%s\n\t[ASCII]\t%s", InHeader.getInHeaderByOp(opcode), opcode, Integer.toHexString(opcode).toUpperCase(), HexUtils.byteArraytoHex(buff.array()), HexUtils.toAscii(buff.array())));
                }

            } catch (Exception e) {
                log.error("PacketReadError", e);
                this.close();
            }


        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable except) {
        if (!(except instanceof ReadTimeoutException) && !(except instanceof IOException)) {
            log.error("An exception occured when reading a packet.", new Exception(except));
        } else {
            if (except instanceof ReadTimeoutException) {
                log.error("User has been disconnected due ReadTimeoutException.");
            }
        }
        this.close();
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
