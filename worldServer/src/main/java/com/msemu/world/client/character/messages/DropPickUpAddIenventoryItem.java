package com.msemu.world.client.character.messages;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.enums.PickUpMessageType;

public class DropPickUpAddIenventoryItem extends DropPickUpMessage {

    private final Item item;

    public DropPickUpAddIenventoryItem(Item item) {
        this.item = item;
    }

    @Override
    public PickUpMessageType getDropPickUpType() {
        return PickUpMessageType.ADD_INVENTORY_ITEM;
    }


    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        super.encode(outPacket);
        outPacket.encodeInt(item.getItemId());
        outPacket.encodeInt(item.getQuantity());
    }
}
