package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestPetReqData extends QuestReqData {

    @Getter
    private List<Integer> petItems = new ArrayList<>();

    @Override
    public QuestRequirementType getType() {
        return QuestRequirementType.pet;
    }
}
