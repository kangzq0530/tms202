package com.msemu.world.enums;

import lombok.Getter;

public enum MobControlLevel {

    NONE(0),
    NORMAL(1),
    FOLLOW(2),;

    @Getter
    private int value;

    MobControlLevel(int value) {
        this.value = value;
    }
}
