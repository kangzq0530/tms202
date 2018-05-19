package com.msemu.core.network.packets.outpacket.user.local.quest;

import com.msemu.world.enums.QuestResultType;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_UpdateQuestResult extends QuestResult {
    public LP_UpdateQuestResult(int questID, int uJobDemandLower,
                                int nextQuestID, boolean inProgress ) {
        super(QuestResultType.UPDATE_QUEST);
        encodeInt(questID);
        encodeInt(uJobDemandLower);
        encodeInt(nextQuestID);
        encodeByte(inProgress);
    }
}
