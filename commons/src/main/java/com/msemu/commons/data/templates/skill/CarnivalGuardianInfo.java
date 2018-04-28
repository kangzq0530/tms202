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
public class CarnivalGuardianInfo implements DatSerializable {
    private int type;
    private int spendCP;
    private String name;
    private String desc;
    private int mobSkillID;
    private int level;

    @Override
    public String toString() {
        return String.format("[怪物擂台守護] %s - %s (%d) 消耗CP: %d 等級: %d", name, desc, mobSkillID, spendCP, level);
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(this.type);
        dos.writeInt(this.spendCP);
        dos.writeInt(this.mobSkillID);
        dos.writeInt(this.level);
        dos.writeUTF(this.name);
        dos.writeUTF(this.desc);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        this.setType(dis.readInt());
        this.setSpendCP(dis.readInt());
        this.setMobSkillID(dis.readInt());
        this.setLevel(dis.readInt());
        this.setName(dis.readUTF());
        this.setDesc(dis.readUTF());
        return this;
    }
}
