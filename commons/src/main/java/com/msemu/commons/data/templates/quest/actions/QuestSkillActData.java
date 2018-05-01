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
public class QuestSkillActData extends QuestActData {

    @Getter
    @Setter
    private int skill;

    @Getter
    @Setter
    private int level = 1;

    @Getter
    @Setter
    private int masterLevel = 1;

    @Getter
    List<Short> jobs = new ArrayList<>();

    @Override
    public QuestActDataType getType() {
        return QuestActDataType.skill;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(skill);
        dos.writeShort(level);
        dos.writeInt(jobs.size());
        for(Short v : jobs)
            dos.writeShort(v);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setSkill(dis.readInt());
        setLevel(dis.readShort());
        int size = dis.readInt();
        for(int i  = 0 ; i < size; i++)
            getJobs().add(dis.readShort());
        return this;
    }
}
