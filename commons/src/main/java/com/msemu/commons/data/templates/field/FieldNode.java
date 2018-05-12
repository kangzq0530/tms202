package com.msemu.commons.data.templates.field;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/5/12.
 */
@Getter
@Setter
public class FieldNode implements DatSerializable {
    private int key, x, y, attr;
    private List<Integer> edges = new ArrayList<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(key);
        dos.writeInt(x);
        dos.writeInt(y);
        dos.writeInt(attr);
        dos.writeInt(edges.size());
        for (Integer v : edges)
            dos.writeInt(v);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setKey(dis.readInt());
        setX(dis.readInt());
        setY(dis.readInt());
        setAttr(dis.readInt());
        int size = dis.readInt();
        for (int i = 0; i < size; i++)
            getEdges().add(dis.readInt());
        return this;
    }
}
