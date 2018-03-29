package com.msemu.commons.network.netty;

import com.msemu.commons.network.InPacket;
import com.msemu.commons.network.crypt.MapleCrypt;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Weber on 2018/3/29.
 */
public class PacketDecoder extends ByteToMessageDecoder {
    private static final Logger log = LoggerFactory.getLogger("Packet");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        NettyClient client = ctx.channel().attr(NettyClient.CLIENT_KEY).get();
        MapleCrypt crypt = ctx.channel().attr(NettyClient.CRYPTO_KEY).get();

        if (client == null || crypt == null)
            return;
        byte[] iv = client.getRecvIV();
        if (client.getStoredLength() == -1) {
            if (in.readableBytes() < 4)
                return;
            int header = in.readInt();
            if (!crypt.checkHeader(header, iv)) {
                log.error(String.format("[PacketDecoder] | Incorrect packet seq! Dropping client %s.", client.getIP()));
                client.close();
            } else {
                client.setStoredLength(crypt.getLength(header));
            }
        } else {
            if (in.readableBytes() < client.getStoredLength()) {
                return;
            }
            byte[] data = new byte[client.getStoredLength()];
            in.readBytes(data);
            client.setStoredLength(-1);
            crypt.crypt(data, iv);
            client.setRecvIV(crypt.getNewIv(iv));
            InPacket inPacket = new InPacket(data);
            list.add(inPacket);
        }
    }
}
