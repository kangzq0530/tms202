package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/4.
 */
public enum MobAppearType {

    Normal(-1),
    Regen(-2),
    Revived(-3),
    Suspended(-4),
    Delay(-5),
    Effect(0),;
    @Getter
    private short value;

    MobAppearType(int value) {
        this.value = (short) value;
    }
}
