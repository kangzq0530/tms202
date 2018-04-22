package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementType;
import com.msemu.commons.data.templates.quest.QuestReqSkill;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestSkillReqData extends QuestReqData {

    @Getter
    private List<QuestReqSkill> skills = new ArrayList<>();

    @Override
    public QuestRequirementType getType() {
        return QuestRequirementType.skill;
    }
}
