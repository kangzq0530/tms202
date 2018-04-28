package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementDataType;
import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/22.
 */
@Getter
@Setter
public class QuestMobReqData extends QuestReqData {

    private int mobId;
    private int count;

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.mob;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(mobId);
        dos.writeInt(count);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setMobId(dis.readInt());
        setCount(dis.readInt());
        return this;
    }
}
