package com.msemu.world.client.character.messages;

import com.msemu.world.enums.PickUpMessageType;

public class DropPickUpDuplicateItemSN extends DropPickUpMessage {
    @Override
    public PickUpMessageType getDropPickUpType() {
        return PickUpMessageType.ITEM_SN_DUPLICATE_FAIL;
    }
}
