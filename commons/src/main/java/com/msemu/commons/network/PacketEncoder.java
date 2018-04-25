package com.msemu.commons.network;

import com.msemu.commons.network.Client;
import com.msemu.commons.network.crypt.ICipher;
import com.msemu.commons.network.packets.Packet;
import com.msemu.core.configs.CoreConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Weber on 2018/3/29.
 */
public class PacketEncoder<TClient extends Client<TClient>> extends MessageToByteEncoder<Packet> {
    private static final Logger log = LoggerFactory.getLogger("Packet");

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf byteBuf) throws Exception {
        byte[] data = packet.getData();
        Client client = ctx.channel().attr(Client.CLIENT_KEY).get();
        ICipher cipher = client.getConnection().getSendCipher();
        if (cipher != null) {
            if (CoreConfig.SHOW_PACKET ) {
                log.warn("[Out]\t|" + packet);
            }
            client.acquireEncoderState();
            try {
                byte[] iv = client.getSiv();
                byte[] head = cipher.getSendHeader(data.length, iv);
                cipher.crypt(data, iv);
                iv = cipher.getNewIv(iv);
                client.setSiv(iv);
                byteBuf.writeBytes(head);
            } finally {
                client.releaseEncodeState();
            }
            byteBuf.writeBytes(data);
        } else {
            byteBuf.writeBytes(data);
        }
    }
}
