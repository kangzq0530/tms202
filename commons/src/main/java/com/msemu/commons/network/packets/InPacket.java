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

package com.msemu.commons.network.packets;


import com.msemu.commons.network.Client;
import com.msemu.commons.utils.HexUtils;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import com.msemu.core.configs.CoreConfig;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created on 2/18/2017.
 */
public abstract class InPacket<TClient extends Client<TClient>> extends Packet<TClient> implements Runnable, Cloneable {
    private static final Logger log = LoggerFactory.getLogger(InPacket.class);
    @Getter
    @Setter
    protected TClient client;
    private ByteBuf byteBuf;
    @Getter
    private short opcode;

    public InPacket(short opcode) {
        this.opcode = opcode;
    }

    public void setByteBuf(ByteBuf byteBuf) {
        setData(byteBuf.array());
        this.byteBuf = byteBuf;
    }

    @Override
    public int getLength() {
        return byteBuf.readableBytes();
    }

    @Override
    public byte[] getData() {
        return byteBuf.array();
    }


    @SuppressWarnings("unchecked")
    public InPacket<TClient> clonePacket() {
        try {
            return (InPacket<TClient>) super.clone();
        } catch (Exception var2) {
            log.error("Error while cloning ReceivablePacket: {}", this.getClass().getSimpleName(), var2);
            return null;
        }
    }


    /**
     * Reads a single byte of the ByteBuf.
     *
     * @return The byte that has been read.
     */
    public byte decodeByte() {
        return byteBuf.readByte();
    }

    /**
     * Reads an <code>amount</code> of bytes from the ByteBuf.
     *
     * @param amount The amount of bytes to read.
     * @return The bytes that have been read.
     */
    public byte[] decodeBytes(int amount) {
        byte[] arr = new byte[amount];
        for (int i = 0; i < amount; i++) {
            arr[i] = byteBuf.readByte();
        }
        return arr;
    }

    /**
     * Reads an integer from the ByteBuf.
     *
     * @return The integer that has been read.
     */
    public int decodeInt() {
        return byteBuf.readIntLE();
    }

    /**
     * Reads a short from the ByteBuf.
     *
     * @return The short that has been read.
     */
    public short decodeShort() {
        return byteBuf.readShortLE();
    }

    /**
     * Reads a char array of a given length of this ByteBuf.
     *
     * @param amount The length of the char array
     * @return The char array as a String
     */
    public String decodeString(int amount) {
        byte[] bytes = decodeBytes(amount);
        return new String(bytes, CoreConfig.GAME_SERVICE_TYPE.getCharset());
    }

    /**
     * Reads a String, by first reading a short, then reading a char array of that length.
     *
     * @return The char array as a String
     */
    public String decodeString() {
        int amount = decodeShort();
        return decodeString(amount);
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean showNow) {
        int start = showNow ? getData().length - available() : 2;
        int size = showNow ? getData().length : getData().length;
        return HexUtils.readableByteArray(Arrays.copyOfRange(getData(), start, size)); // Substring after copy of range xd
    }


    /**
     * Reads and returns a long from this packet.
     *
     * @return The long that has been read.
     */
    public long decodeLong() {
        return byteBuf.readLongLE();
    }

    /**
     * Reads a position (short x, short y) and returns this.
     *
     * @return The position that has been read.
     */
    public Position decodePosition() {
        return new Position(decodeShort(), decodeShort());
    }

    /**
     * Reads a rectangle (short l, short t, short r, short b) and returns this.
     *
     * @return The rectangle that has been read.
     */
    public Rect decodeShortRect() {
        return new Rect(decodePosition(), decodePosition());
    }

    /**
     * Reads a rectangle (int l, int t, int r, int b) and returns this.
     *
     * @return The rectangle that has been read.
     */
    public Position decodePositionInt() {
        return new Position(decodeInt(), decodeInt());
    }

    /**
     * Returns the amount of bytes that are unread.
     *
     * @return The amount of bytes that are unread.
     */
    public int getUnreadAmount() {
        return byteBuf.readableBytes();
    }


    public void skip(int offset) {
        byteBuf.skipBytes(offset);
    }

    public final int available() {
        return byteBuf.readableBytes();
    }

    public abstract void read();

    public abstract void runImpl();

    public void run() {
        try {
            this.runImpl();
        } catch (Exception var2) {
            log.error("Error while running {} packet", this.getClass().getSimpleName(), var2);
        }

    }

}

