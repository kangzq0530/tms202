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
public class FieldDirectionInfo implements DatSerializable {

    private int x, y;
    private boolean forcedInput;
    private List<String> eventQs = new ArrayList<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(x);
        dos.writeInt(y);
        dos.writeBoolean(forcedInput);
        dos.writeInt(eventQs.size());
        for (String s : eventQs)
            dos.writeUTF(s);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setX(dis.readInt());
        setY(dis.readInt());
        setForcedInput(dis.readBoolean());
        int size = dis.readInt();
        for (int i = 0; i < size; i++)
            getEventQs().add(dis.readUTF());
        return this;
    }
}
