package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementType;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestNormalStartReqData extends QuestReqData {

    @Override
    public QuestRequirementType getType() {
        return QuestRequirementType.normalAutoStart;
    }
}
