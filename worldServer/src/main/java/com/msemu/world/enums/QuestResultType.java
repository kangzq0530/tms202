package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/4/29.
 */
public enum QuestResultType {

    ADD_QUEST_TIMER(7),
    REMOVE_QUEST_TIMER(8),
    ADD_AND_KEEP_QUEST_TIMER(9),
    REMOVE_AND_KEEP_QUEST_TIMER(10),
    UPDATE_QUEST(11),;

    @Getter
    private byte value;

    QuestResultType(int value) {
        this.value = (byte) value;
    }

}
