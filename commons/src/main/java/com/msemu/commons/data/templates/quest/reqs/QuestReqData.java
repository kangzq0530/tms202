package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementType;
import lombok.Getter;

/**
 * Created by Weber on 2018/4/22.
 */
public abstract class QuestReqData {


    public QuestReqData() {
    }

    public abstract QuestRequirementType getType();
}
