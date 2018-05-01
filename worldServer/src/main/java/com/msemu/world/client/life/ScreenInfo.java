package com.msemu.world.client.life;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/11.
 */
@Getter
@Setter
public class ScreenInfo {
    private byte type;
    private int value;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(getType());
        outPacket.encodeInt(getValue());
    }
}
