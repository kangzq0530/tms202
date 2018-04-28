package com.msemu.commons.data.templates.field;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/24.
 */
@Getter
@Setter
public class LifeData implements DatSerializable {
    private String type = "";
    private int id;
    private int x, y, f, fh, cy, rx0, rx1;
    private int mobTime, mobAliveReq, regenStart;
    private boolean hide, useDay, useNight, hold, nofoothold, dummy, spine, mobTimeOnDie;
    private String limitedname = "";

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(type);
        dos.writeUTF(limitedname);
        dos.writeInt(id);
        dos.writeInt(x);
        dos.writeInt(y);
        dos.writeInt(f);
        dos.writeInt(fh);
        dos.writeInt(cy);
        dos.writeInt(rx0);
        dos.writeInt(rx1);
        dos.writeInt(mobTime);
        dos.writeInt(mobAliveReq);
        dos.writeInt(regenStart);
        dos.writeBoolean(hide);
        dos.writeBoolean(useDay);
        dos.writeBoolean(useNight);
        dos.writeBoolean(hold);
        dos.writeBoolean(nofoothold);
        dos.writeBoolean(dummy);
        dos.writeBoolean(spine);
        dos.writeBoolean(mobTimeOnDie);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setType(dis.readUTF());
        setLimitedname(dis.readUTF());
        setId(dis.readInt());
        setX(dis.readInt());
        setY(dis.readInt());
        setF(dis.readInt());
        setFh(dis.readInt());
        setCy(dis.readInt());
        setRx0(dis.readInt());
        setRx1(dis.readInt());
        setMobTime(dis.readInt());
        setMobAliveReq(dis.readInt());
        setRegenStart(dis.readInt());
        setHide(dis.readBoolean());
        setUseDay(dis.readBoolean());
        setUseNight(dis.readBoolean());
        setHold(dis.readBoolean());
        setNofoothold(dis.readBoolean());
        setDummy(dis.readBoolean());
        setSpine(dis.readBoolean());
        setMobTimeOnDie(dis.readBoolean());
        return this;
    }
}
