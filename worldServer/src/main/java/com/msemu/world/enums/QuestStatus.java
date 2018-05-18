package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/13.
 */
public enum QuestStatus {
    NOT_STARTED(0),
    STARTED(1),
    COMPLETE(2);

    private byte value;

    QuestStatus(int val) {
        this.value = (byte) val;
    }

    public byte getValue() {
        return value;
    }
}

