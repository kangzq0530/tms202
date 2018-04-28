package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementDataType;
import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestLevelMaxReqData extends QuestReqData {

    @Getter
    @Setter
    private int maxLevel;

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.lvmax;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(maxLevel);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setMaxLevel(dis.readInt());
        return this;
    }
}
