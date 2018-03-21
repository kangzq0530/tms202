package com.msemu.commons.io;

import com.msemu.commons.data.provider.streams.ByteArrayByteStream;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Weber on 2018/3/16.
 */
public class ByteArrayByteStreamTest extends TestCase {
    @Test
    public void testGetPosition() {
        byte[] ary = new byte[]{1, 2, 3, 4, 5, 6, 7};
        ByteArrayByteStream stream = new ByteArrayByteStream(ary);
        assertEquals(0, stream.getPosition());
    }

    @Test
    public void testSeek() {
        byte[] ary = new byte[]{1, 2, 3, 4, 5, 6, 7};
        ByteArrayByteStream stream = new ByteArrayByteStream(ary);
        stream.seek(5);
        assertEquals(5, stream.getPosition());
    }

    @Test
    public void testGetBytesRead() {
        byte[] ary = new byte[]{1, 2, 3, 4, 5, 6, 7};
        ByteArrayByteStream stream = new ByteArrayByteStream(ary);
        stream.readByte();
        assertEquals(1, stream.getBytesRead());
        stream.readByte();
        stream.readByte();
        assertEquals(3, stream.getBytesRead());
        stream.unReadByte();
        assertEquals(2, stream.getBytesRead());
    }

    @Test
    public void testReadByte() {
        byte[] ary = new byte[]{1, 2, 3, 4, 5, 6, 7};
        ByteArrayByteStream stream = new ByteArrayByteStream(ary);
        assertEquals(1, stream.readByte());
        assertEquals(2, stream.readByte());
    }

    @Test
    public void testUnReadByte() {

        byte[] ary = new byte[]{1, 2, 3, 4, 5, 6, 7};
        ByteArrayByteStream stream = new ByteArrayByteStream(ary);
        stream.readByte();
        stream.unReadByte();
        assertEquals(1, stream.readByte());
    }

    @Test
    public void testReadLastByte() {
        byte[] ary = new byte[]{1, 2, 3, 4, 5, 6, 7};
        ByteArrayByteStream stream = new ByteArrayByteStream(ary);
        stream.readByte();
        assertEquals(1, stream.readLastByte());
    }

    @Test
    public void testReadLastBytes() {

        byte[] ary = new byte[]{1, 2, 3, 4, 5, 6, 7};
        ByteArrayByteStream stream = new ByteArrayByteStream(ary);

        stream.readByte();
        stream.readByte();

        int[] data = stream.readLastBytes(2);

        assertEquals(1, data[0]);
        assertEquals(2, data[1]);
    }

    @Test
    public void testAvailable() {
        byte[] ary = new byte[]{1, 2, 3, 4, 5, 6, 7};
        ByteArrayByteStream stream = new ByteArrayByteStream(ary);
        assertEquals(7, stream.available());
    }

}