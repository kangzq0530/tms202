package com.msemu.commons.data.enums;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/11.
 */

public enum PortalType {
    // http://mapleref.wikia.com/wiki/Portal
    StartPoint(0),
    PortalInvisible(1),
    PortalVisible(2),
    PortalCollision(3),
    PortalChangable(4),
    PortalChangableInvisible(5),
    TownPortalPoint(6),
    PortalScript(7),
    PortalScriptInvisible(8),
    PortalCollisionScript(9),
    PortalHidden(10),
    PortalScriptHidden(11),
    PortalCollisionJump(12),
    PortalCollisionCustom(13),
    PortalCollisionInvisibleChangable(14), // ?
    PortalUnk(15), // ? On map ID 910800200
    ;
    private byte value;

    PortalType(byte val) {
        this.value = val;
    }

    PortalType(int val) {
        this((byte) val);
    }

    public byte getValue() {
        return value;
    }

    public static PortalType getTypeByInt(int i) {
        return Arrays.stream(values()).filter(p -> p.getValue() == i).findFirst().orElse(null);
    }
}
