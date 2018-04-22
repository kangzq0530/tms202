package com.msemu.world.enums;

import com.msemu.world.client.character.quest.act.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Weber on 2018/4/13.
 */
public enum QuestActionType {
    EXP(0),
    ITEM(1),
    MONEY(2),
    POP(3)
    ;

    private byte val;

    QuestActionType(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }

    public static QuestActionType getQPRTByObj(Object o) {
        return o instanceof QuestExpAction ? EXP :
                o instanceof QuestItemAction ? ITEM :
                        o instanceof QuestMoneyAction ? MONEY :
                                o instanceof QuestPopAction ? POP : null;
    }

    public static QuestActionType getQPRTByVal(byte val) {
        return Arrays.stream(QuestActionType.values())
                .filter(qprt -> qprt.getVal() == val).findFirst().orElse(null);
    }


    public IQuestAction load(DataInputStream dis) throws IOException {
        switch(this) {
            case EXP:
                return (IQuestAction) new QuestExpAction().load(dis);
            case ITEM:
                return (IQuestAction) new QuestItemAction().load(dis);
            case MONEY:
                return (IQuestAction) new QuestMoneyAction().load(dis);
            case POP:
                return (IQuestAction) new QuestPopAction().load(dis);
            default:
                return null;
        }
    }
}
