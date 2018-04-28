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
public class QuestQuestReqData extends QuestReqData {

    private int questId;
    private byte state;
    private int order;

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.quest;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(questId);
        dos.writeByte(state);
        dos.writeInt(order);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setQuestId(dis.readInt());
        setState(dis.readByte());
        setOrder(dis.readInt());
        return this;
    }
}
