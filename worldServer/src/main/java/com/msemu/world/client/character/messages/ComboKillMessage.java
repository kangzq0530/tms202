package com.msemu.world.client.character.messages;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.WvsMessageType;

/**
 * Created by Weber on 2018/5/5.
 */
public class ComboKillMessage implements IWvsMessage {

    private int count;
    private int mobID;

    public ComboKillMessage(int count, int mobID) {
        this.count = count;
        this.mobID = mobID;
    }

    @Override
    public WvsMessageType getType() {
        return WvsMessageType.StylishKillMessage;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(1);
        outPacket.encodeInt(count);
        outPacket.encodeInt(mobID);
        outPacket.encodeInt(3000);
        outPacket.encodeInt(mobID);
    }
}
