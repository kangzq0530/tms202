package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/13.
 */
public enum InventoryOperationType {
    ADD(0),
    UPDATE_QUANTITY(1),
    MOVE(2),
    REMOVE(3),
    ITEM_EXP(4),
    UPDATE_BAG_POS(5),
    UPDATE_BAG_QUANTITY(6),
    UNK_1(7),
    UPDATE_ITEM_INFO(8),
    UNK_2(9),
    UNK_3(10),
    ;

    private byte value;

    InventoryOperationType(int val) {
        this.value = (byte) val;
    }

    public byte getValue() {
        return value;
    }
}
