package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestLevelMaxReqData extends QuestReqData {

    @Getter
    @Setter
    private int maxLevel;

    @Override
    public QuestRequirementType getType() {
        return QuestRequirementType.lvmax;
    }
}
