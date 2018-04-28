package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementDataType;
import com.msemu.commons.data.loader.dat.DatSerializable;

/**
 * Created by Weber on 2018/4/22.
 */
public abstract class QuestReqData implements DatSerializable {


    public QuestReqData() {
    }

    public abstract QuestRequirementDataType getType();
}
