package com.msemu.commons.network.packets;

import com.msemu.commons.network.Client;
import com.msemu.commons.utils.HexUtils;

import java.util.Arrays;

/**
 * Created by Weber on 2018/3/23.
 */
public class Packet<TClient extends Client<TClient>> {
    private byte[] data;

    public Packet() {
        data = new byte[0];
    }

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

    public void setData(byte[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        if (data == null) return "";
        return "[Packet] | " + HexUtils.readableByteArray(data);
    }

}
