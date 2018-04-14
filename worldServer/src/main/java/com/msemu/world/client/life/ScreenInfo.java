package com.msemu.world.client.life;

import com.msemu.commons.network.packets.OutPacket;

/**
 * Created by Weber on 2018/4/11.
 */
public class ScreenInfo {
    private byte type;
    private int value;

    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(getType());
        outPacket.encodeInt(getValue());
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
