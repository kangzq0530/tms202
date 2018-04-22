package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementType;
import com.msemu.commons.data.templates.quest.QuestReqMob;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestMobReqData extends QuestReqData {

    @Getter
    private List<QuestReqMob> mobs = new ArrayList<>();

    @Override
    public QuestRequirementType getType() {
        return QuestRequirementType.mob;
    }
}
