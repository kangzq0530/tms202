package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementDataType;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestNormalStartReqData extends QuestReqData {

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.normalAutoStart;
    }
}
