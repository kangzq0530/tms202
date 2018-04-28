package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementDataType;
import com.msemu.commons.data.loader.dat.DatSerializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestNormalStartReqData extends QuestReqData {

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.normalAutoStart;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {

    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        return this;
    }
}
