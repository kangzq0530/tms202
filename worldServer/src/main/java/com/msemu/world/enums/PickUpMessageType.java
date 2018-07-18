package com.msemu.world.enums;

import lombok.Getter;

public enum PickUpMessageType {
    ITEM_SN_DUPLICATE_FAIL(-6),
    OWNERSHIP_FAIL_OTHER_ITEM(-5),
    OWNERSHIP_FAIL(-4),
    ITEM_CRC_FAIL(-3),
    MAX_COUNT_FAIL(-2),
    ITEM_FULL_FAIL(-1),
    ADD_INVENTORY_ITEM(0x0),
    MESSO(0x1),
    CONSUME(0x2),
    DONE(0x3),
    NO_MSG(0x4),
    MESSO_TO_BOAT(8);
    @Getter
    private final int value;

    PickUpMessageType(int value) {
        this.value = value;
    }

}
