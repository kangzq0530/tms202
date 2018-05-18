package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/28.
 */
public enum AvatarModifiedMask {
    AvatarLook(0x1),
    Speed(0x2),
    CarryItemEffect(0x4),
    SubAvatarLook(0x8),;

    private byte val;

    AvatarModifiedMask(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }
}
