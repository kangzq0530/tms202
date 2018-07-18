package com.msemu.world.enums;

import lombok.Getter;

import java.util.Arrays;

public enum  EquipmentEnchantType {

    SCROLL(0),  // 卷軸，咒文痕跡
    STAR_FORCE(1),

    SCROLL_REQUEST_DISPLAY(0x32),


    UNKNOWN(0xFF),

    ;
    @Getter
    private final int value;

    EquipmentEnchantType(int value) {
        this.value = value;
    }

    public static EquipmentEnchantType getByValue(final int value) {
        return Arrays.stream(values()).filter(i -> i.getValue() == value)
                .findFirst().orElse(UNKNOWN);
    }
}
