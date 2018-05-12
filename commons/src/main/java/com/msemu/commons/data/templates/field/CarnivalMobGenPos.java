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
public class CarnivalMobGenPos implements DatSerializable {
    private int x, y, fh, cy;

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(x);
        dos.writeInt(y);
        dos.writeInt(fh);
        dos.writeInt(cy);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setX(dis.readInt());
        setY(dis.readInt());
        setFh(dis.readInt());
        setCy(dis.readInt());
        return this;
    }
}
