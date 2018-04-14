package com.msemu.commons.network.packets;

import com.msemu.commons.utils.HexUtils;

/**
 * Created by Weber on 2018/3/23.
 */
public class Packet implements Cloneable {
    private byte[] data;

    public Packet(byte[] data) {
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public int getLength() {
        if (data != null) {
            return data.length;
        }
        return 0;
    }

    public int getHeader() {
        if (data.length < 2) {
            return 0xFFFF;
        }
        return (data[0] + (data[1] << 8));
    }

    public void setData(byte[] nD) {
        data = nD;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        if (data == null) return "";
        return "[Packet] | " + HexUtils.readableByteArray(data);
    }

    @Override
    public Packet clone() {
        return new Packet(data);
    }
}
