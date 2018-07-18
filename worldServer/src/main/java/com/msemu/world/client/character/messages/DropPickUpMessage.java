package com.msemu.world.client.character.messages;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PickUpMessageType;
import com.msemu.world.enums.WvsMessageType;

public abstract class DropPickUpMessage implements IWvsMessage {
    @Override
    public WvsMessageType getType() {
        return WvsMessageType.DropPickUpMessage;
    }

    public abstract PickUpMessageType getDropPickUpType();

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(0);
        outPacket.encodeByte(getDropPickUpType().getValue());
    }
}
