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

import com.msemu.commons.enums.OutHeader;
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
            OutHeader op = OutHeader.getOutHeaderByOp(packet.getHeader());
            if (CoreConfig.SHOW_PACKET && (op == null || !op.ignoreDebug())) {
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
