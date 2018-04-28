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
public class QuestIntervalReqData extends QuestReqData {

    @Getter
    @Setter
    private int interval;

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.interval;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(interval);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setInterval(dis.readInt());
        return this;
    }
}
