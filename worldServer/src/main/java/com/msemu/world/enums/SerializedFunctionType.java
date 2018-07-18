package com.msemu.world.enums;

import lombok.Getter;

import java.util.Arrays;

public enum SerializedFunctionType {
    // 美髮沙龍
    PREVIEW_SALON_REQUEST(0x6d530290),
    PREVIEW_SALON_RESPONSE(0xeb70f39c),
    CHANGE_SALON_RESPONSE(0x2c504c33),
    UPDATE_SALON_RESPONSE(0x2e3c4666),
    STORE_SALON_REQUEST(0xf6ec0ec0),
    // 楓方塊




    REMOVE_CASH_ITEM_REQUEST(0x1bfb9030),;
    @Getter
    private final int value;

    SerializedFunctionType(int value) {
        this.value = value;
    }

    public static SerializedFunctionType getByValue(final int value) {
        return Arrays.stream(values()).filter(i -> i.getValue() == value)
                .findFirst().orElse(null);
    }
}
