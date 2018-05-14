package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/13.
 */
public enum DeathType {
    STAY(0),
    ANIMATION_DEATH(1),
    NO_ANIMATION_DEATH(2),
    INSTA_DEATH(3),
    NO_ANIMATION_DEATH_2(4),
    ANIMATION_DEATH_2(5),
    ANIMATION_DEATH_3(6),

    ;
    private byte value;

    DeathType(byte val) {
        this.value = val;
    }

    DeathType(int val) {
        this((byte) val);
    }

    public byte getValue() {
        return value;
    }

    public  static DeathType getByValue(int value) {
        for(DeathType type : values()) {
            if(type.getValue() == value) {
                return type;
            }
        }
        return ANIMATION_DEATH;
    }
}