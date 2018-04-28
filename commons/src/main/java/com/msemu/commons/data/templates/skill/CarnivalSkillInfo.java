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
public class CarnivalSkillInfo implements DatSerializable{
    private int target;
    private int skillID;
    private int level;
    private String name;
    private String desc;


    @Override
    public String toString() {
        return String.format("[怪物擂台技能] %s - $s (%d) 等級: %d", name, desc, skillID, level);
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(this.target);
        dos.writeInt(this.skillID);
        dos.writeInt(this.level);
        dos.writeUTF(this.name);
        dos.writeUTF(this.desc);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        this .setTarget(dis.readInt());
        this.setSkillID(dis.readInt());
        this.setLevel(dis.readInt());
        this.setName(dis.readUTF());
        this.setDesc(dis.readUTF());
        return this;
    }
}
