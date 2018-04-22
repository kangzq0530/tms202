package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementType;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestPartyRankSReqData extends QuestReqData {

    final int[] partyQuests = new int[]{1200, 1201, 1202, 1203, 1204, 1205, 1206, 1300, 1301, 1302};

    @Override

    public QuestRequirementType getType() {
        return QuestRequirementType.partyQuest_S;
    }
}
