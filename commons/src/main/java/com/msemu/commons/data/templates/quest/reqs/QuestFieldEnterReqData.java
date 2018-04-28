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
public class QuestFieldEnterReqData extends QuestReqData {

    @Getter
    private List<Long> fields = new ArrayList<>();

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.fieldEnter;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(fields.size());
        for(Long val : fields)
            dos.writeLong(val);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        int fieldSize = dis.readInt();
        for(int i = 0 ; i < fieldSize; i++) {
            getFields().add(dis.readLong());
        }
        return this;
    }
}
