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

import com.msemu.commons.network.crypt.ICipher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Weber on 2018/3/29.
 */
public class PacketDecoder<TClient extends Client<TClient>> extends ByteToMessageDecoder {
    private static final Logger log = LoggerFactory.getLogger("Packet");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        Client client = ctx.channel().attr(Client.CLIENT_KEY).get();
        ICipher crypt = client.getConnection().getRecvCipher();
        if (crypt == null)
            return;
        byte[] iv = client.getRiv();
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
            client.setRiv(crypt.getNewIv(iv));
            list.add(data);
        }
    }
}
