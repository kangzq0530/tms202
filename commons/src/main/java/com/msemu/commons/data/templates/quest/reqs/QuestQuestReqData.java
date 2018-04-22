package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementType;
import com.msemu.commons.data.templates.quest.QuestReqState;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestQuestReqData extends QuestReqData {

    @Getter
    List<QuestReqState> quests = new ArrayList<>();

    @Override
    public QuestRequirementType getType() {
        return QuestRequirementType.quest;
    }
}
