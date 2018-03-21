package com.msemu.commons.data.provider.streams;

import com.msemu.core.configs.CoreConfig;

import java.awt.*;
import java.io.ByteArrayOutputStream;

/**
 * Created by Weber on 2018/3/16.
 */
public class LittleEndianAccessor {
    private final ByteArrayByteStream bs;

    public LittleEndianAccessor(final ByteArrayByteStream stream) {
        this.bs = stream;
    }

    public int readByteAsInt() {
        return bs.readByte();
    }

    public final byte readByte() {
        return (byte) bs.readByte();
    }

    public final int readInt() {
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        final int byte3 = bs.readByte();
        final int byte4 = bs.readByte();
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    public final long readUInt() {
        int value = readInt();
        long value2 = 0;
        if (value < 0) {
            value2 += 0x80000000L;
        }
        return (value & 0x7FFFFFFF) + value2;
    }

    public final short readShort() {
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        return (short) ((byte2 << 8) + byte1);
    }

    public final int readUShort() {
        short value = readShort();
        int value2 = 0;
        if (value < 0) {
            value2 += 0x8000;
        }
        return (value & 0x7FFF) + value2;
    }

    public final char readChar() {
        return (char) readShort();
    }


    public final long readLong() {
        final long byte1 = bs.readByte();
        final long byte2 = bs.readByte();
        final long byte3 = bs.readByte();
        final long byte4 = bs.readByte();
        final long byte5 = bs.readByte();
        final long byte6 = bs.readByte();
        final long byte7 = bs.readByte();
        final long byte8 = bs.readByte();

        return (byte8 << 56) + (byte7 << 48) + (byte6 << 40) + (byte5 << 32) + (byte4 << 24) + (byte3 << 16)
                + (byte2 << 8) + byte1;
    }


    public final float readFloat() {
        return Float.intBitsToFloat(readInt());
    }


    public final double readDouble() {
        return Double.longBitsToDouble(readLong());
    }


    public final String readAsciiString(final int n) {
        final byte ret[] = new byte[n];
        for (int x = 0; x < n; x++) {
            ret[x] = readByte();
        }
        return new String(ret, CoreConfig.GAME_SERVICE_TYPE.getCharset());
    }

    public final long getBytesRead() {
        return bs.getBytesRead();
    }

    public final String readMapleAsciiString() {
        return readAsciiString(readShort());
    }


    public final Point readPos() {
        final int x = readShort();
        final int y = readShort();
        return new Point(x, y);
    }

    public final byte[] read(final int num) {
        byte[] ret = new byte[num];
        for (int x = 0; x < num; x++) {
            ret[x] = readByte();
        }
        return ret;
    }

    public final void unReadByte() {
        bs.unReadByte();
    }

    public final void unReadInt() {
        for (int byte_ = 0; byte_ < 4; byte_++) {
            bs.unReadByte();
        }
    }

    public final void unReadShort() {
        for (int byte_ = 0; byte_ < 2; byte_++) {
            bs.unReadByte();
        }
    }

    public final void unReadLong() {
        for (int byte_ = 0; byte_ < 8; byte_++) {
            bs.unReadByte();
        }
    }

    public final void unReadAsciiString(final int n) {
        for (int byte_ = 0; byte_ < n; byte_++) {
            bs.unReadByte();
        }
    }

    public final void unReadPos() {
        for (int byte_ = 0; byte_ < 4; byte_++) {
            bs.unReadByte();
        }
    }

    public final void unRead(final int num) {
        for (int byte_ = 0; byte_ < num; byte_++) {
            bs.unReadByte();
        }
    }

    public final byte readLastByte() {
        this.unReadByte();
        return (byte) bs.readLastByte();
    }

    public final int readLastInt() {
        unReadInt();
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        final int byte3 = bs.readByte();
        final int byte4 = bs.readByte();
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    public final short readLastShort() {
        unReadShort();
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        return (short) ((byte2 << 8) + byte1);
    }

    public final long readLastLong() {
        unReadLong();
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        final int byte3 = bs.readByte();
        final int byte4 = bs.readByte();
        final long byte5 = bs.readByte();
        final long byte6 = bs.readByte();
        final long byte7 = bs.readByte();
        final long byte8 = bs.readByte();

        return (byte8 << 56)
                + (byte7 << 48)
                + (byte6 << 40)
                + (byte5 << 32)
                + (byte4 << 24)
                + (byte3 << 16)
                + (byte2 << 8)
                + byte1;
    }

    public final String readLastAsciiString(final int n) {
        for (int y = 0; y < n; y++) {
            unReadByte();
        }
        final byte ret[] = new byte[n];
        for (int x = 0; x < n; x++) {
            ret[x] = (byte) readByte();
        }
        return new String(ret, CoreConfig.GAME_SERVICE_TYPE.getCharset());
    }

    public final Point readLastPos() {
        unReadInt();
        short x = readShort();
        short y = readShort();
        return new Point(x, y);
    }

    public final byte[] readLastBytes(final int num) {
        for (int byte_ = 0; byte_ < num; byte_++) {
            bs.unReadByte();
        }
        byte[] ret = new byte[num];
        for (int x = 0; x < num; x++) {
            ret[x] = readByte();
        }
        return ret;
    }

    public final long available() {
        return bs.available();
    }

    @Override
    public final String toString() {
        return bs.toString();
    }

    public final String toString(final boolean b) {
        return bs.toString(b);
    }


    public final void seek(final long offset) {
        bs.seek(offset);
    }

    public final long getPosition() {
        return bs.getPosition();
    }

    public final void skip(final int num) {
        seek(getPosition() + num);
    }

    public final String readNullTerminatedAsciiString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte b;
        while (true) {
            b = readByte();
            if (b == 0) {
                break;
            }
            baos.write(b);
        }
        byte[] buf = baos.toByteArray();
        char[] chrBuf = new char[buf.length];
        for (int x = 0; x < buf.length; x++) {
            chrBuf[x] = (char) buf[x];
        }
        return String.valueOf(chrBuf);
    }
}
