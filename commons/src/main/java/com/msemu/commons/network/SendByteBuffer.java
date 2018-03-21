package com.msemu.commons.network;

import com.msemu.core.configs.CoreConfig;
import com.msemu.core.configs.NetworkConfig;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Weber on 2018/3/14.
 */

public class SendByteBuffer {
    private ByteBuffer buffer;

    public SendByteBuffer() {
        this.buffer = ByteBuffer.allocateDirect(NetworkConfig.SEND_BUFFER_SIZE);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    protected static byte[] hex2Byte(String str) {
        byte[] bytes = new byte[str.length() / 2];

        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);
        }

        return bytes;
    }

    public int position() {
        return this.buffer.position();
    }

    public Buffer position(int pos) {
        return this.buffer.position(pos);
    }

    public void limit(int limit) {
        this.buffer.limit(limit);
    }

    public int limit() {
        return this.buffer.limit();
    }

    public int remaining() {
        return this.buffer.remaining();
    }

    public boolean hasRemaining() {
        return this.buffer.hasRemaining();
    }

    public void flip() {
        this.buffer.flip();
    }

    public int capacity() {
        return this.buffer.capacity();
    }

    public void compact() {
        this.buffer.compact();
    }

    public void clear() {
        this.buffer.clear();
    }

    public final void write(byte value) {
        this.buffer.put(value);
    }

    public final void write(boolean value) {
        this.buffer.put((byte) (value ? 1 : 0));
    }

    public final void write(int value) {
        this.buffer.put((byte) value);
    }

    public final void writeShort(boolean value) {
        this.buffer.putShort((short) (value ? 1 : 0));
    }

    public final void writeShort(int value) {
        this.buffer.putShort((short) value);
    }

    public final void writeInt(boolean value) {
        this.buffer.putInt(value ? 1 : 0);
    }

    public final void writeInt(int value) {
        this.buffer.putInt(value);
    }

    public final void writeInt(long value) {
        this.buffer.putInt((int) (value & -1L));
    }

    public final void writeLong(boolean value) {
        this.buffer.putLong(value ? 1L : 0L);
    }

    public final void writeLong(long value) {
        this.buffer.putLong(value);
    }

    public final void writeFloat(float value) {
        this.buffer.putFloat(value);
    }

    public final void writeFloat(double value) {
        this.buffer.putFloat((float) value);
    }

    public final void writeFloat(int value) {
        this.buffer.putFloat((float) value);
    }

    public final void writeBytes(byte[] data) {
        this.buffer.put(data);
    }

    public final void writeZeroBytes(int size) {
        this.buffer.put(new byte[size]);
    }

    public final void writeAsciiString(String value) {
        value = getLimitedString(value, Short.MAX_VALUE);
        byte[] data = value.getBytes(CoreConfig.GAME_SERVICE_TYPE.getCharset());
        writeBytes(data);
    }

    public final void writeAsciiString(String value, int max) {
        max = Math.min(max, Short.MAX_VALUE);
        value = getLimitedString(value, max);
        byte[] data = value.getBytes(CoreConfig.GAME_SERVICE_TYPE.getCharset());
        writeBytes(data);
        writeZeroBytes(max - data.length);
    }

    public final void writeMapleAsciiString(String value) {
        value = getLimitedString(value, Short.MAX_VALUE);
        byte[] data = value.getBytes(CoreConfig.GAME_SERVICE_TYPE.getCharset());
        writeShort(data.length);
        writeBytes(data);
    }

    private String getLimitedString(String s, final int max) {
        String string = "";
        int stringLength = 0;
        String tempString;
        for (char c : s.toCharArray()) {
            tempString = String.valueOf(c);
            stringLength += tempString.getBytes(CoreConfig.GAME_SERVICE_TYPE.getCharset()).length;
            if (stringLength <= max) {
                string += tempString;
            } else {
                break;
            }
        }
        return string;
    }

    private void putChars(CharSequence charSequence) {
        if (charSequence != null) {
            int length = charSequence.length();

            for (int i = 0; i < length; ++i) {
                this.buffer.putChar(charSequence.charAt(i));
            }

        }
    }

    public ByteBuffer getBuffer() {
        return this.buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SendByteBuffer)) {
            return false;
        } else {
            SendByteBuffer other = (SendByteBuffer) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                ByteBuffer this$buffer = this.getBuffer();
                ByteBuffer other$buffer = other.getBuffer();
                if (this$buffer == null) {
                    if (other$buffer != null) {
                        return false;
                    }
                } else if (!this$buffer.equals(other$buffer)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof SendByteBuffer;
    }

    public int hashCode() {
        boolean PRIME = true;
        byte result = 1;
        ByteBuffer $buffer = this.getBuffer();
        int result1 = result * 59 + ($buffer == null ? 43 : $buffer.hashCode());
        return result1;
    }

    public String toString() {
        return "SendByteBuffer(buffer=" + this.getBuffer() + ")";
    }
}