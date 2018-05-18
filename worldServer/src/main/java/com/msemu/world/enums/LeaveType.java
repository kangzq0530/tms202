package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/13.
 */
public enum LeaveType {
    NO_ANIMATION(0),
    ANIMATION(1),;


    private byte value;

    LeaveType(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}

