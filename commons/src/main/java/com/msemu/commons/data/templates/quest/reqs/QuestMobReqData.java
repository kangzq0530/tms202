package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementDataType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestMobReqData extends QuestReqData {
    @Getter
    @Setter
    private int mobId;
    @Getter
    @Setter
    private int count;

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.mob;
    }
}
