package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/11.
 */
public enum DropType {

    MONEY(0),
    ITEM(1),
    ;

    private byte val;

    DropType(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }
}