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
public class FieldArea implements DatSerializable {
    private String name = "";
    private int x1, x2, y1, y2;


    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(name);
        dos.writeInt(x1);
        dos.writeInt(y1);
        dos.writeInt(x2);
        dos.writeInt(y2);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setName(dis.readUTF());
        setX1(dis.readInt());
        setY1(dis.readInt());
        setX2(dis.readInt());
        setY2(dis.readInt());
        return this;
    }
}
