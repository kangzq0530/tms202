package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/13.
 */
public enum InventoryOperationType {
    ADD(0),
    UPDATE(1),
    MOVE(2),
    REMOVE(3),
    ITEM_EXP(4),
    MOVE_TO_BAG(5),
    UPDATE_IN_BAG(6),
    REMOVE_IN_BAG(7),
    MOVE_IN_BAG(8),
    ADD_IN_BAG(9),;

    private byte value;

    InventoryOperationType(int val) {
        this.value = (byte) val;
    }

    public byte getValue() {
        return value;
    }
}
