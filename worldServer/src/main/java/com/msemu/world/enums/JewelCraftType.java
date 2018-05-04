package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/3.
 */
public enum JewelCraftType {
    INSTALL_SUCCESS(0),
    INSTALL_FAIL(1),
    SYNTHESIZE_SUCCESS(2),
    SYNTHESIZE_KEEP(3),
    SYNTHESIZE_FAIL(4),
    ERROR(5),
    ;
    @Getter
    private byte value;

    JewelCraftType(int value) {
        this.value = (byte) value;
    }
}
