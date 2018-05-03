package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/4.
 */
public enum DropMotionType {
    DMT_NORMAL(0),
    DMT_BONUS_FLOW(0x1),
    DMT_BONUS_THROW (0x2),
    DMT_SSFS_FLOW(0x3),
    ;
    @Getter
    private byte val;

    DropMotionType(int val) {
        this.val= (byte) val;
    }
}
