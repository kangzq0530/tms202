package com.msemu.world.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/29.
 */
public enum FaceEmotion {

    ;
    @Getter
    private int value;

    private FaceEmotion(int value) {
        this.value = value;
    }

    public static FaceEmotion getByValue(int emotion) {
        return Arrays.stream(values()).filter(e -> e.getValue() == emotion)
                .findFirst().orElse(null);
    }
}
