package com.msemu.commons.data.templates.quest.actions;

import com.msemu.commons.data.enums.QuestActDataType;
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
public class QuestSpActData extends QuestActData {

    @Getter
    @Setter
    private short sp;

    @Getter
    private List<Short> jobs = new ArrayList<>();


    @Override
    public QuestActDataType getType() {
        return QuestActDataType.sp;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeShort(sp);
        dos.writeInt(jobs.size());
        for (Short v : jobs)
            dos.writeShort(v);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setSp(dis.readShort());
        int size = dis.readInt();
        for (int i = 0; i < size; i++)
            getJobs().add(dis.readShort());
        return this;
    }
}
