package com.msemu.world.enums;

import com.msemu.world.client.character.quest.req.*;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/13.
 */
public enum QuestStartRequirementType {

    QUEST(0),
    ITEM(1),
    JOB(2),
    MARRIAGE(3),
    MAX_LEVEL(4),
    MIN_STAT(5);

    private byte val;

    QuestStartRequirementType(int val) {
        this.val = (byte) val;
    }

    public static QuestStartRequirementType getQPRTByObj(Object o) {
        return o instanceof QuestStartCompletionRequirement ? QUEST :
                o instanceof QuestStartItemRequirement ? ITEM :
                        o instanceof QuestStartJobRequirement ? JOB :
                                o instanceof QuestStartMarriageRequirement ? MARRIAGE :
                                        o instanceof QuestStartMaxLevelRequirement ? MAX_LEVEL :
                                                o instanceof QuestStartMinStatRequirement ? MIN_STAT
                                                        : null;
    }

    public static QuestStartRequirementType getQPRTByVal(byte val) {
        return Arrays.stream(QuestStartRequirementType.values())
                .filter(qprt -> qprt.getVal() == val).findFirst().orElse(null);
    }

    public byte getVal() {
        return val;
    }
}
