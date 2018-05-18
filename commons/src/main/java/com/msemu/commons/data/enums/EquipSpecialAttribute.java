package com.msemu.commons.data.enums;

/**
 * Created by Weber on 2018/4/13.
 */
public enum EquipSpecialAttribute {
    NO_DESTROY_EXCEPT_ENHANCE(0x1),
    ALWAYS_TIER_UP(0x2),
    ALWAYS_SCROLL_SUCCEED(0x4),
    TRACE(0x80)
    ;
    private int value;

    EquipSpecialAttribute(final int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
}

