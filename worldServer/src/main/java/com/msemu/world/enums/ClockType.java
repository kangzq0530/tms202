package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/12.
 */
public enum ClockType {
    EVENT_TIMER(0),

    TIMER_COUNTER(3),;
    @Getter
    private int value;

    ClockType(int value) {
        this.value = value;
    }

    public static ClockType getByValue(int value) {
        for (ClockType type : values()) {
            if (type.getValue() == value)
                return type;
        }
        return null;
    }
}
