package com.msemu.commons.data.provider.streams;

import com.msemu.commons.utils.HexUtils;

import java.nio.ByteBuffer;

/**
 * Created by Weber on 2018/3/16.
 */
public class ByteArrayByteStream implements IByteStream {

    private final ByteBuffer buffer;

    public ByteArrayByteStream(final byte[] array) {
        buffer = ByteBuffer.wrap(array);
    }

    @Override
    public long getPosition() {
        return buffer.position();
    }

    @Override
    public void seek(long offset) {
        buffer.position((int) offset);
    }

    @Override
    public long getBytesRead() {
        return buffer.position();
    }

    @Override
    public int readByte() {
        return buffer.get() & 0xFF;
    }

    @Override
    public void unReadByte() {
        buffer.position(buffer.position() - 1);
    }

    @Override
    public int readLastByte() {
        unReadByte();
        int data = readByte();
        return data;
    }

    @Override
    public int[] readLastBytes(int bytes) {
        int[] data = new int[bytes];
        buffer.position(buffer.position() - bytes);
        for (int i = 0; i < bytes; i++)
            data[i] = buffer.get() & 0xFF;
        return data;
    }

    @Override
    public long available() {
        return buffer.remaining();
    }


    public String toString(final boolean b) {
        String nows = "";
        if (buffer.remaining() > 0) {
            byte[] now = new byte[buffer.remaining()];
            System.arraycopy(buffer.array(), buffer.remaining(), now, 0, buffer.array().length - buffer.position());
            nows = HexUtils.byteArraytoHex(now);
        }
        if (b) {
            return "全部: " + HexUtils.byteArraytoHex(buffer.array()) + " 現在: " + nows;
        } else {
            return "數據: " + nows;
        }
    }
}
