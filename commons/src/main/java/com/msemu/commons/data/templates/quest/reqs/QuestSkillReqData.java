package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementDataType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestSkillReqData extends QuestReqData {

    @Getter
    @Setter
    private int skillId;

    @Getter
    @Setter
    private byte level;

    @Getter
    @Setter
    private String levelCondition;

    @Getter
    @Setter
    private boolean acquire;

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.skill;
    }
}
