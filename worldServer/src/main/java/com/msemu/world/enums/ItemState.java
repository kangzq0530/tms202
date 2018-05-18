package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/13.
 */
public enum ItemState {
    NOT_ENHANCABLE(0),
    ENHANCABLE(0x100),;

    private int value;

    ItemState(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
}
