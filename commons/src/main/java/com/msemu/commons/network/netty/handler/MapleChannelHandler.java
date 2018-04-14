package com.msemu.commons.network.netty.handler;

import com.msemu.commons.network.netty.NettyClient;
import com.msemu.commons.enums.InHeader;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.HexUtils;
import com.msemu.core.configs.CoreConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Weber on 2018/3/29.
 */
public abstract class MapleChannelHandler<TClient extends NettyClient<TClient>> extends ChannelInboundHandlerAdapter implements IChannelHandler<TClient> {

    private static Logger log = LoggerFactory.getLogger(MapleChannelHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        TClient client = (TClient) ctx.channel().attr(NettyClient.CLIENT_KEY).get();
        client.onClose();
        log.debug("Session Closed from {}", client.getIP());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        TClient client = (TClient) ctx.channel().attr(NettyClient.CLIENT_KEY).get();
        client.onOpen();
        log.warn("Session Opened from {}", client.getIP());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            log.warn("Client forcibly closed the game.");
        } else {
            cause.printStackTrace();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            TClient client = (TClient) ctx.channel().attr(NettyClient.CLIENT_KEY).get();
            InPacket inPacket = (InPacket) msg;
            if (client == null || inPacket == null) {
                return;
            }
            Short op = inPacket.decodeShort();
            InHeader opcode = InHeader.getInHeaderByOp(op);
            if (opcode == null) {
                handleUnknown(op, inPacket);
            } else {
                handlePacket(opcode, inPacket, client);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    protected void handleUnknown(short opCode, InPacket inPacket) {
        if (!InHeader.isSpamHeader(InHeader.getInHeaderByOp(opCode))) {
            log.warn(String.format("Unhandled opcode %s/0x%s, packet %s", opCode, Integer.toHexString(opCode), inPacket));
        }
    }

    public void handlePacket(InHeader opcode, InPacket packet, TClient client) {
        if (CoreConfig.SHOW_PACKET && !InHeader.isSpamHeader(opcode)) {
            log.warn(String.format("[In]\t| %s, 0x%s\t| %s\n\t%s", opcode, Integer.toHexString(opcode.getValue()).toUpperCase(), packet, HexUtils.toAscii(packet.getData())));
        }
    }
}
