package com.msemu.commons.data.enums;

/**
 * Created by Weber on 2018/4/22.
 */
public enum QuestActDataType {
    UNDEFINED,
    exp,
    item,
    nextQuest,
    money,
    quest,
    skill,
    pop,
    buffItemID,
    infoNumber,
    sp,
    charismaEXP,
    charmEXP,
    willEXP,
    insightEXP,
    senseEXP,
    craftEXP,
    transferField,;

    public static QuestActDataType getQuestActionDataByName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException ex) {
            return UNDEFINED;
        }
    }
}
