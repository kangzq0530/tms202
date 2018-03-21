package com.msemu.commons.io;

import com.msemu.commons.data.provider.streams.ByteArrayByteStream;
import com.msemu.commons.data.provider.streams.LittleEndianAccessor;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Weber on 2018/3/16.
 */
public class LittleEndianAccessorTest extends TestCase {
    @Test
    public void testReadByteAsInt() {
        byte[] data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        LittleEndianAccessor lea = new LittleEndianAccessor(new ByteArrayByteStream(data));
        for (int i = 1; i <= data.length; i++) {
            assertEquals(i, lea.readByte());
        }
    }

    @Test
    public void testReadByte() {
        byte[] data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        LittleEndianAccessor lea = new LittleEndianAccessor(new ByteArrayByteStream(data));
        for (int i = 1; i <= data.length; i++) {
            assertEquals(i, lea.readByteAsInt());
        }
    }

    @Test
    public void testReadInt() {
        byte[] data = new byte[]{1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0};
        LittleEndianAccessor lea = new LittleEndianAccessor(new ByteArrayByteStream(data));
        for (int i = 1; i <= 3; i++) {
            assertEquals(i, lea.readInt());
        }
    }

    @Test
    public void testReadUInt() {

    }

    @Test
    public void testReadShort() {

    }

    @Test
    public void testReadUShort() {

    }

    @Test
    public void testReadChar() {

    }

    @Test
    public void testReadLong() {

    }

    @Test
    public void testReadFloat() {

    }

    @Test
    public void testReadDouble() {

    }

    @Test
    public void testReadAsciiString() {

    }

    @Test
    public void testGetBytesRead() {

    }

    @Test
    public void testReadMapleAsciiString() {

    }

    @Test
    public void testReadPos() {

    }

    @Test
    public void testRead() {

    }

    @Test
    public void testUnReadByte() {

    }

    @Test
    public void testUnReadInt() {

    }

    @Test
    public void testUnReadShort() {

    }

    @Test
    public void testUnReadLong() {

    }

    @Test
    public void testUnReadAsciiString() {

    }

    @Test
    public void testUnReadPos() {

    }

    @Test
    public void testUnRead() {

    }

    @Test
    public void testReadLastByte() {

    }

    @Test
    public void testReadLastInt() {

    }

    @Test
    public void testReadLastShort() {

    }

    @Test
    public void testReadLastLong() {

    }

    @Test
    public void testReadLastAsciiString() {

    }

    @Test
    public void testReadLastPos() {

    }

    @Test
    public void testReadLastBytes() {

    }

    @Test
    public void testAvailable() {

    }

    @Test
    public void testSeek() {

    }

    @Test
    public void testGetPosition() {

    }

    @Test
    public void testSkip() {

    }

    @Test
    public void testReadNullTerminatedAsciiString() {

    }

}