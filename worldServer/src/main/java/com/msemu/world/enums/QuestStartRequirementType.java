package com.msemu.world.enums;

import com.msemu.world.client.character.quest.requirements.*;

import java.io.DataInputStream;
import java.io.IOException;
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
    MIN_STAT(5)
    ;

    private byte val;

    QuestStartRequirementType(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
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


    public IQuestStartRequirement load(DataInputStream dis) throws IOException {
        switch(this) {
            case QUEST:
                return (IQuestStartRequirement) new QuestStartCompletionRequirement().load(dis);
            case ITEM:
                return (IQuestStartRequirement) new QuestStartItemRequirement().load(dis);
            case JOB:
                return (IQuestStartRequirement) new QuestStartJobRequirement().load(dis);
            case MARRIAGE:
                return (IQuestStartRequirement) new QuestStartMarriageRequirement().load(dis);
            case MAX_LEVEL:
                return (IQuestStartRequirement) new QuestStartMaxLevelRequirement().load(dis);
            case MIN_STAT:
                return (IQuestStartRequirement) new QuestStartMinStatRequirement().load(dis);
            default:
                return null;
        }
    }
}
