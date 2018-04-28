package com.msemu.commons.data.templates.skill;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/23.
 */
@Getter
@Setter
public class ReqSkill implements DatSerializable{

    private int level;
    private int reqAmount;
    private int skillID;


    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(level);
        dos.writeInt(reqAmount);
        dos.writeInt(skillID);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setLevel(dis.readInt());
        setReqAmount(dis.readInt());
        setSkillID(dis.readInt());
        return this;
    }
}
