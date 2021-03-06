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

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.Client;
import com.msemu.commons.utils.HexUtils;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import com.msemu.core.configs.CoreConfig;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by Weber on 2018/3/29.
 */
public class OutPacket<TClient extends Client<TClient>> extends Packet<TClient> {
    private static final Logger log = LogManager.getRootLogger();
    @Getter
    @Setter
    private TClient client;
    private ByteArrayOutputStream baos;
    private boolean loopback = false;
    private boolean encryptedByShanda = false;
    private short opcode;

    /**
     * Creates a new OutPacket with a given opcode. Immediately encodes the opcode.
     *
     * @param op The opcode of this OutPacket.
     */
    public OutPacket(short op) {
        super(new byte[]{});
        baos = new ByteArrayOutputStream();
        encodeShort(op);
        this.opcode = op;
    }

    /**
     * Creates a new OutPacket with a given opcode. Immediately encodes the opcode.
     *
     * @param op The opcode of this OutPacket.
     */
    public OutPacket(int op) {
        this((short) op);
    }

    /**
     * Creates a new OutPacket, and initializes the data as empty.
     */
    public OutPacket() {
        super(new byte[]{});
        baos = new ByteArrayOutputStream();
    }

    /**
     * Creates a new OutPacket with given data.
     *
     * @param data The data this packet has to be initialized with.
     */
    public OutPacket(byte[] data) {
        super(data);
        baos = new ByteArrayOutputStream();
        encodeArr(data);
    }

    /**
     * Creates a new OutPacket with a given header. Immediately encodes the header's short value.
     *
     * @param header The header of this OutPacket.
     */
    public OutPacket(OutHeader header) {
        this(header.getValue());
    }

    /**
     * Returns the header of this OutPacket.
     *
     * @return the header of this OutPacket.
     */
    @Override
    public int getHeader() {
        return opcode;
    }

    /**
     * Encodes a single byte to this OutPacket.
     *
     * @param b The int to encode as a byte. Will be downcast, so be careful.
     */
    public void encodeByte(int b) {
        encodeByte((byte) b);
    }

    /**
     * Encodes a byte to this OutPacket.
     *
     * @param b The byte to encode.
     */
    public void encodeByte(byte b) {
        baos.write(b);
    }

    /**
     * Encodes a byte array to this OutPacket.
     * Named like this to prevent autocompletion of "by" to "byteArray" or similar names.
     *
     * @param bArr The byte array to encode.
     */
    public void encodeArr(byte[] bArr) {
        for (byte b : bArr) {
            baos.write(b);
        }
    }

    /**
     * Encodes a character to this OutPacket.
     *
     * @param c The character to encode
     */
    public void encodeChar(char c) {
        baos.write(c);
    }

    /**
     * Encodes a boolean to this OutPacket.
     *
     * @param b The boolean to encode (0/1)
     */
    public void encodeByte(boolean b) {
        baos.write(b ? 1 : 0);
    }

    /**
     * Encodes a short to this OutPacket, in little endian.
     *
     * @param s The short to encode.
     */
    public void encodeShort(short s) {
        baos.write((byte) s);
        baos.write((byte) (s >>> 8));
    }

    public void encodeShortBE(short s) {
        baos.write(s);
    }

    public void encodeIntBE(int i) {
        baos.write((byte) (i >>> 24));
        baos.write((byte) (i >>> 16));
        baos.write((byte) (i >>> 8));
        baos.write((byte) i);
    }

    /**
     * Encodes an integer to this OutPacket, in little endian.
     *
     * @param i The integer to encode.
     */
    public void encodeInt(int i) {
        baos.write((byte) i);
        baos.write((byte) (i >>> 8));
        baos.write((byte) (i >>> 16));
        baos.write((byte) (i >>> 24));
    }

    /**
     * Encodes a long to this OutPacket, in little endian.
     *
     * @param l The long to encode.
     */
    public void encodeLong(long l) {
        baos.write((byte) l);
        baos.write((byte) (l >>> 8));
        baos.write((byte) (l >>> 16));
        baos.write((byte) (l >>> 24));
        baos.write((byte) (l >>> 32));
        baos.write((byte) (l >>> 40));
        baos.write((byte) (l >>> 48));
        baos.write((byte) (l >>> 56));
    }

    /**
     * Encodes a FileTime to this OutPacket.
     *
     * @param fileTime The FileTime to encode.
     */
    public void encodeLong(FileTime fileTime) {
        encodeFT(fileTime);
    }

    /**
     * Encodes a String to this OutPacket.
     * Structure: short(size) + char array of <code>s</code>.
     *
     * @param str The String to encode.
     */
    public void encodeString(String str) {
        byte[] data = str != null ? str.getBytes(CoreConfig.GAME_SERVICE_TYPE.getCharset()) : new byte[]{};
        if (data.length > Short.MAX_VALUE) {
            log.error("Tried to encode a string that is too big.");
            return;
        }
        encodeShort((short) data.length);
        encodeArr(data);
    }

    /**
     * Writes a String as a character array to this OutPacket.
     * If <code>s.length()</code> is smaller than length, the open spots are filled in with zeros.
     *
     * @param s      The String to encode.
     * @param length The maximum length of the buffer.
     */
    public void encodeString(String s, short length) {
        if (s == null) {
            s = "";
        }
        byte[] data = s.getBytes(CoreConfig.GAME_SERVICE_TYPE.getCharset());
        if (data.length > 0) {
            for (byte b : data) {
                encodeByte(b);
            }
        }
        for (int i = data.length; i < length; i++) {
            encodeByte((byte) 0);
        }
    }

    @Override
    public byte[] getData() {
        return baos.toByteArray();
    }

    @Override
    public void setData(byte[] data) {
        super.setData(data);
        baos.reset();
        encodeArr(data);
    }

    /**
     * Returns the length of the ByteArrayOutputStream.
     *
     * @return The length of baos.
     */
    @Override
    public int getLength() {
        return baos.size();
    }

    public boolean isLoopback() {
        return loopback;
    }

    public boolean isEncryptedByShanda() {
        return encryptedByShanda;
    }

    @Override
    public String toString() {
        return String.format("%s, 0x%s\t| Length: %d\n\t[ALL]\t%s\n\t[ASCII]\t%s", OutHeader.getOutHeaderByOp(opcode), Integer.toHexString(opcode).toUpperCase(), getLength() - 2
                , HexUtils.readableByteArray(Arrays.copyOfRange(getData(), 2, getData().length)), HexUtils.toAscii(getData()));
    }

    public void encodeShort(int value) {
        encodeShort((short) value);
    }

    public void encodeString(String name, int length) {
        encodeString(name, (short) length);
    }

    public void encodeFT(FileTime fileTime) {
        if (fileTime == null) {
            encodeLong(0);
        } else {
            encodeInt(fileTime.getLowDateTime());
            encodeInt(fileTime.getHighDateTime());
        }
    }

    public void encodePosition(Position position) {
        if (position != null) {
            encodeShort(position.getX());
            encodeShort(position.getY());
        } else {
            encodeShort(0);
            encodeShort(0);
        }
    }

    public void encodeRectInt(Rect rect) {
        encodeInt(rect.getLeft());
        encodeInt(rect.getTop());
        encodeInt(rect.getRight());
        encodeInt(rect.getBottom());
    }

    public void encodePositionInt(Position position) {
        encodeInt(position.getX());
        encodeInt(position.getY());
    }

    public void encodeFT(long currentTime) {
        FileTime fileTime = new FileTime(currentTime);
        encodeFT(fileTime);
    }

    public void encodeTime(boolean dynamicTerm, int time) {
        encodeByte(dynamicTerm);
        encodeInt(time);
    }

    public void encodeTime(int time) {
        encodeByte(false);
        encodeInt(time);
    }

    public void encodeRect(Rect rect) {
        encodeInt(rect.getLeft());
        encodeInt(rect.getTop());
        encodeInt(rect.getRight());
        encodeInt(rect.getBottom());
    }

    public void encodeZeroBytes(int count) {
        for (int i = 0; i < count; i++)
            encodeByte(0);
    }
}
