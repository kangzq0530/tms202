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
public class RadderRope implements DatSerializable {
    private int x;
    private int y1, y2;
    private int page;
    private int piece;

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(x);
        dos.writeInt(y1);
        dos.writeInt(y2);
        dos.writeInt(page);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setX(dis.readInt());
        setY1(dis.readInt());
        setY2(dis.readInt());
        setPage(dis.readInt());
        return this;
    }
}
