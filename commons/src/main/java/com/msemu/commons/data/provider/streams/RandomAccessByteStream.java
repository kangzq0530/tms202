package com.msemu.commons.data.provider.streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Weber on 2018/3/16.
 */
public class RandomAccessByteStream implements IByteStream {

    private final RandomAccessFile file;
    private long read = 0;

    public RandomAccessByteStream(final RandomAccessFile file) {
        this.file = file;
    }

    public final byte[] toByteArray() throws IOException {
        int nRead;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[16384];
        while ((nRead = file.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    @Override
    public long getPosition() throws IOException {
        return file.getFilePointer();
    }

    @Override
    public void seek(long offset) throws IOException {
        file.seek(offset);
    }

    @Override
    public long getBytesRead() {
        return read;
    }

    @Override
    public int readByte() throws IOException {
        int temp = file.read();
        read++;
        return temp;
    }

    @Override
    public void unReadByte() throws IOException {
        seek(getPosition() - 1);
        read--;
    }

    @Override
    public int readLastByte() throws IOException {
        seek(getPosition() - 1);
        int data = readByte();
        return data;
    }

    @Override
    public int[] readLastBytes(int bytes) throws IOException {
        int[] data = new int[bytes];
        seek(getPosition() - bytes);
        for (int i = 0; i < bytes; i++) {
            data[i] = readByte();
        }
        return data ;
    }

    @Override
    public long available() throws IOException {
        return file.length() - file.getFilePointer();
    }
}
