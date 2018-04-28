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
public class QuestJobReqData extends QuestReqData {

    @Getter
    private List<Short> jobs = new ArrayList<>();


    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.job;
    }

    public void addJob(int job) {
        getJobs().add((short) job);
    }


    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(getJobs().size());
        for(Short v: jobs) {
            dos.writeShort(v);
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        int jobSize = dis.readInt();
        for(int i = 0; i < jobSize; i++)
            addJob(dis.readShort());
        return this;
    }
}
