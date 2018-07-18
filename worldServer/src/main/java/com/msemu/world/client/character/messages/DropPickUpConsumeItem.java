package com.msemu.world.client.character.messages;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.enums.PickUpMessageType;

public class DropPickUpConsumeItem extends DropPickUpMessage {

    private final Item item;

    public DropPickUpConsumeItem(Item item) {
        this.item = item;
    }

    @Override
    public PickUpMessageType getDropPickUpType() {
        return PickUpMessageType.CONSUME;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        super.encode(outPacket);
        outPacket.encodeInt(item.getItemId());
    }
}
