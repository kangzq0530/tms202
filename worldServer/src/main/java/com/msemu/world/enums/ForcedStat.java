package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/12.
 */
public enum ForcedStat {

    STR(0x1),
    DEX(0x2),
    INT(0x4),
    LUK(0x8),
    WATK(0x10),
    WDEF(0x20),
    MATK(0x40),
    MDEF(0x80),
    ACC(0x100),
    AVOID(0x200),
    SPEED(0x400), // byte
    JUMP(0x800), // byte
    UNKNOWN(0x1000); // byte

    @Getter
    private final int value;

    ForcedStat(int value) {
        this.value = value;
    }

}
