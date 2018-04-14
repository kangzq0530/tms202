package com.msemu.login.enums;

/**
 * Created by Weber on 2018/4/14.
 */
public enum PicStatus {
    CREATE_PIC(0),
    ENTER_PIC(1),
    IGNORE(2),
    OUTDATED(4),
    ;

    private byte value;

    PicStatus(byte val) {
        this.value = val;
    }

    PicStatus(int val) {
        this((byte) val);
    }

    public byte getValue() {
        return value;
    }
}
