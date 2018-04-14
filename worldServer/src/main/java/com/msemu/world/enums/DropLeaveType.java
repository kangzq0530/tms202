package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/13.
 */
public enum DropLeaveType {
    IDK_0(0),
    IDK_1(1),
    CHAR_PICKUP_1(2),
    CHAR_PICKUP_2(3),
    DELAYED_PICKUP(4),
    PET_PICKUP(5),
    IDK_2(6),
    IDK_3(7), // CAnimationDisplayer::RegisterAbsorbItemAnimationJP ?
    ;

    private byte value;

    DropLeaveType(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
