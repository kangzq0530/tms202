package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestDateEndReqData extends QuestReqData {
    @Getter
    @Setter
    private long date;

    @Override
    public QuestRequirementType getType() {
        return QuestRequirementType.end;
    }
}
