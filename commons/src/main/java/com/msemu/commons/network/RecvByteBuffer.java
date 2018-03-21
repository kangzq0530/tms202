package com.msemu.commons.network;

import com.msemu.commons.utils.HexUtils;
import com.msemu.core.configs.CoreConfig;

import java.awt.*;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * Created by Weber on 2018/3/14.
 */
public class RecvByteBuffer {
    private final ByteBuffer buffer;

    public RecvByteBuffer(ByteBuffer byteBuffer) {
        this.buffer = byteBuffer;
    }

    public final ByteBuffer getBuffer() {
        return this.buffer;
    }

    public final int getAvailableBytes() {
        return this.getBuffer().remaining();
    }

    public final void skip(int bytes) {
        if (this.buffer.remaining() < bytes) {
            throw new BufferUnderflowException();
        } else {
            this.buffer.position(this.buffer.position() + bytes);
        }
    }

    public final void skipAll() {
        this.buffer.position(this.buffer.limit());
    }

    public final void readBytes(byte[] dst) {
        this.buffer.get(dst);
    }

    public final byte[] readBytes(int size) {
        byte[] tmp = new byte[size];
        this.buffer.get(tmp);
        return tmp;
    }

    public final void readBytes(byte[] dst, int offset, int len) {
        this.buffer.get(dst, offset, len);
    }

    public final byte readByte() {
        return (byte) (this.buffer.get() & 255);
    }

    public final int readByteAsInt() {
        return this.buffer.get() & 255;
    }

    public final boolean readByteAsBoolean() {
        return (this.buffer.get() & 255) == 1;
    }


    public final short readShort() {
        return this.buffer.getShort();
    }

    public final int readInt() {
        return this.buffer.getInt();
    }

    public final long readLong() {
        return this.buffer.getLong();
    }

    public final float readFloat() {
        return this.buffer.getFloat();
    }

    public final String readAsciiString(final int size) {
        final byte[] tmp = new byte[size];
        readBytes(tmp);
        return new String(tmp, CoreConfig.GAME_SERVICE_TYPE.getCharset());
    }

    public final String readMapleAsciiString() {
        final short size = readShort();
        return readAsciiString(size);
    }

    public final Point readPos() {
        final int x = readShort();
        final int y = readShort();
        return new Point(x, y);
    }

    @Override
    public String toString() {
        int curr = buffer.position();
        buffer.position(0);
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        buffer.position(curr);
        return HexUtils.byteArraytoHex(data);
    }
}
