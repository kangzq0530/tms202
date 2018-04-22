package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementType;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestDayByDayReqData extends QuestReqData {

    @Override
    public QuestRequirementType getType() {
        return QuestRequirementType.dayByDay;
    }
}
