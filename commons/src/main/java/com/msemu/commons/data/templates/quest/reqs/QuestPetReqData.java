package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementDataType;
import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestPetReqData extends QuestReqData {

    @Getter
    private List<Integer> petItems = new ArrayList<>();

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.pet;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(petItems.size());
        for (Integer val : petItems)
            dos.writeInt(val);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        int petItemsSize = dis.readInt();
        for(int i = 0 ; i < petItemsSize; i++)
            petItems.add(dis.readInt());
        return this;
    }
}
