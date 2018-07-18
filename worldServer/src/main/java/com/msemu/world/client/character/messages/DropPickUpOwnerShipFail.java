package com.msemu.world.client.character.messages;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Drop;
import com.msemu.world.enums.PickUpMessageType;

public class DropPickUpOwnerShipFail extends DropPickUpMessage {

    @Override
    public PickUpMessageType getDropPickUpType() {
        return PickUpMessageType.OWNERSHIP_FAIL;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        super.encode(outPacket);
    }
}
