package com.msemu.commons.network.packets;


import com.msemu.commons.network.Client;
import com.msemu.commons.utils.HexUtils;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created on 2/18/2017.
 */
public abstract class InPacket<TClient extends Client<TClient>> extends Packet<TClient> implements Runnable, Cloneable{
    private static final Logger log = LoggerFactory.getLogger(InPacket.class);
    private ByteBuf byteBuf;
    @Getter
    @Setter
    protected TClient client;
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
        char[] chars = new char[amount];
        for (int i = 0; i < amount; i++) {
            chars[i] = (char) bytes[i];
        }
        return String.valueOf(chars);
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
        return toString(false);
    }

    public String toString(boolean showNow) {
        int start = showNow ? byteBuf.arrayOffset() : 0;
        int size = showNow ? byteBuf.readableBytes() : getData().length;
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

