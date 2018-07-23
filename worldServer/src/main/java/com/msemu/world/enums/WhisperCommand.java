package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/12.
 */
public enum WhisperCommand {

    Find(0x5),
    Whisper(0x6),
    FindFriend(0x44),
    NONE(0xFF);
    @Getter
    private int value;

    WhisperCommand(int value) {
        this.value = value;
    }

    public static WhisperCommand getByValue(int value) {
        for (WhisperCommand cmd : values()) {
            if (cmd.getValue() == value)
                return cmd;
        }
        return NONE;
    }
}
