package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.messages.IWvsMessage;
import com.msemu.world.enums.WvsMessageType;

public class MultiKillMessage implements IWvsMessage {

    private final int bonusExp;
    private final int count;

    public MultiKillMessage(int bonusExp, int count) {
        this.bonusExp = bonusExp;
        this.count = count;
    }

    @Override
    public WvsMessageType getType() {
        return WvsMessageType.StylishKillMessage;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(0);
        outPacket.encodeLong(bonusExp);
        outPacket.encodeInt(count);
        outPacket.encodeInt(count);
        outPacket.encodeInt(0);
    }
}
