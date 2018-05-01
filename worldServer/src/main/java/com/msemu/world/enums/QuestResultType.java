package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/4/29.
 */
public enum QuestResultType {

    AddQuestTimer(7),
    RemoveQuestTimer(8),
    AddAndKeepQuestTimer(9),
    RemoveAndKeepQuestTimer(10),
    UpdateQuest(11),;

    @Getter
    private byte value;

    QuestResultType(int value) {
        this.value = (byte) value;
    }

}
