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
public class FieldNodeInfo implements DatSerializable{

    private int start = -1;
    private int end = -1;
    private List<FieldNode> nodes = new ArrayList<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(start);
        dos.writeInt(end);
        dos.writeInt(nodes.size());
        for(FieldNode n : nodes)
            n.write(dos);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setStart(dis.readInt());
        setEnd(dis.readInt());
        int size = dis.readInt();
        for(int i = 0 ; i< size; i++)
            getNodes().add((FieldNode) new FieldNode().load(dis));
        return this;
    }
}
