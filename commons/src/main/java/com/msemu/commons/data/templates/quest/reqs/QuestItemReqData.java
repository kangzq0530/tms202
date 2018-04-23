package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementDataType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestItemReqData  extends QuestReqData {

    @Getter
    @Setter
    private int itemId;

    @Getter
    @Setter
    private int quantity;

    @Getter
    @Setter
    private int order;

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.item;
    }
}
