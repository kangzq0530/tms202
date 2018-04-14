package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/14.
 */
public enum MoveAbility {
    STATIC(0),

    FOLLOW(1),

    SLOW_FORWARD(2),

    FLY_AROUND_CHAR(3),

    FLY_AWAY(4),

    FLY_AROUND_CHAR_2(5),

    THROW(6),

    FIND_NEAREST_MOB(7), ;

    private byte val;

    MoveAbility(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }
}
