package com.msemu.commons.data.enums;

/**
 * Created by Weber on 2018/4/22.
 */
public enum QuestActionType {
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


    public static QuestActionType getByName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException ex) {
            return UNDEFINED;
        }
    }
}
