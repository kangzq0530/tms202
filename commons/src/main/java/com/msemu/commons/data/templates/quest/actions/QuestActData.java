package com.msemu.commons.data.templates.quest.actions;

import com.msemu.commons.data.enums.QuestActDataType;
import com.msemu.commons.data.loader.dat.DatSerializable;

/**
 * Created by Weber on 2018/4/22.
 */
public abstract class QuestActData implements DatSerializable {

    public abstract QuestActDataType getType();

}
