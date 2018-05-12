package com.msemu.commons.data.templates.field;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/5/12.
 */
@Getter
@Setter
public class CarnivalMob implements DatSerializable {
    private int templateID;
    private int spendCP;
    private int mobCount;

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(templateID);
        dos.writeInt(spendCP);
        dos.writeInt(mobCount);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setTemplateID(dis.readInt());
        setSpendCP(dis.readInt());
        setMobCount(dis.readInt());
        return this;
    }
}
