package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/4.
 */
public enum DropMotionType {
    Normal(0),
    BonusFlow(0x1),
    BonusThrow(0x2),
    SsfsFlow(0x3),
    ;
    @Getter
    private byte val;

    DropMotionType(int val) {
        this.val= (byte) val;
    }
}
