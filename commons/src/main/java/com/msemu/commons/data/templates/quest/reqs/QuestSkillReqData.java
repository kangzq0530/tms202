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
public class QuestSkillReqData extends QuestReqData {


    private int skillId;
    private byte level;
    private String levelCondition;
    private boolean acquire;

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.skill;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(skillId);
        dos.writeByte(level);
        dos.writeUTF(levelCondition);
        dos.writeBoolean(acquire);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setSkillId(dis.readInt());
        setLevel(dis.readByte());
        setLevelCondition(dis.readUTF());
        setAcquire(dis.readBoolean());
        return this;
    }
}
