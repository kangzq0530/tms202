package com.msemu.world.enums;

import com.msemu.world.client.character.quest.rewards.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Weber on 2018/4/13.
 */
public enum QuestRewardType {
    EXP(0),
    ITEM(1),
    MONEY(2),
    POP(3)
    ;

    private byte val;

    QuestRewardType(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }

    public static QuestRewardType getQPRTByObj(Object o) {
        return o instanceof QuestExpReward ? EXP :
                o instanceof QuestItemReward ? ITEM :
                        o instanceof QuestMoneyReward ? MONEY :
                                o instanceof QuestPopReward ? POP : null;
    }

    public static QuestRewardType getQPRTByVal(byte val) {
        return Arrays.stream(QuestRewardType.values())
                .filter(qprt -> qprt.getVal() == val).findFirst().orElse(null);
    }


    public IQuestReward load(DataInputStream dis) throws IOException {
        switch(this) {
            case EXP:
                return (IQuestReward) new QuestExpReward().load(dis);
            case ITEM:
                return (IQuestReward) new QuestItemReward().load(dis);
            case MONEY:
                return (IQuestReward) new QuestMoneyReward().load(dis);
            case POP:
                return (IQuestReward) new QuestPopReward().load(dis);
            default:
                return null;
        }
    }
}
