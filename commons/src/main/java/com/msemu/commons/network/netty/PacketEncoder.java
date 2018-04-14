package com.msemu.commons.network.netty;

import com.msemu.commons.network.packets.Packet;
import com.msemu.commons.network.crypt.MapleCrypt;
import com.msemu.core.configs.CoreConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Weber on 2018/3/29.
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    private static final Logger log = LoggerFactory.getLogger("Packet");

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf byteBuf) throws Exception {
        byte[] data = packet.getData();
        NettyClient client = ctx.channel().attr(NettyClient.CLIENT_KEY).get();
        MapleCrypt crypt = ctx.channel().attr(NettyClient.CRYPTO_KEY).get();

        if (client != null) {
            if (CoreConfig.SHOW_PACKET ) {
                log.warn("[Out]\t" + packet);
            }
            byte[] iv = client.getSendIV();
            byte[] head = crypt.getSendHeader(data.length, iv);
            client.acquireEncoderState();
            try {
                crypt.crypt(data, iv);
                iv = crypt.getNewIv(iv);
                client.setSendIV(iv);
            } finally {
                client.releaseEncodeState();
            }

            byteBuf.writeBytes(head);
            byteBuf.writeBytes(data);
        } else {
            byteBuf.writeBytes(data);
        }
    }
}
