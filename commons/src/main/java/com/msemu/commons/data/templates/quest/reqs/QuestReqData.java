package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementDataType;

/**
 * Created by Weber on 2018/4/22.
 */
public abstract class QuestReqData {


    public QuestReqData() {
    }

    public abstract QuestRequirementDataType getType();
}
