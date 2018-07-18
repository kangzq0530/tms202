package com.msemu.world.enums;

import lombok.Getter;

import java.util.Arrays;

public enum BeautySalonOperationType {
    CHANGE(1),
    LOAD(3),
    UPDATE(4),
    STORE(7),
    ;

    @Getter
    private final int value;

    BeautySalonOperationType(int value) {
        this.value = value;
    }

    public static BeautySalonOperationType getByValue(final int value) {
        return Arrays.stream(values()).filter(i -> i.getValue() == value)
                .findFirst().orElse(null);
    }
}
