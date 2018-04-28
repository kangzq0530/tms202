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
@Getter
@Setter
public class QuestPetTamenessMinReqData extends QuestReqData {

    private int petTamenessMin;

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.pettamenessmin;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(petTamenessMin);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setPetTamenessMin(dis.readInt());
        return this;
    }
}
