package com.msemu.commons.data.enums;

/**
 * Created by Weber on 2018/4/22.
 */
public enum QuestRequirementDataType {

    UNDEFINED,
    job,
    item,
    quest,
    lvmin,
    lvmax,
    end,
    end_t,
    mob,
    npc,
    endmeso,
    fieldEnter,
    interval,
    startscript,
    endscript,
    pet,
    pettamenessmin,
    mbmin,
    questComplete,
    pop,
    skill,
    subJobFlags,
    dayByDay,
    normalAutoStart,
    partyQuest_S,
    charmMin,
    senseMin,
    craftMin,
    willMin,
    charismaMin,
    insightMin,
    job_TW;

    public static QuestRequirementDataType getByName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException ex) {
            return UNDEFINED;
        }
    }
}
