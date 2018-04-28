package com.msemu.commons.data.templates;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/28.
 */
@Getter
public class ForbiddenName implements DatSerializable {

    private List<String> names = new ArrayList<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(names.size());
        for(String val : names)
            dos.writeUTF(val);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        for(int i = 0 ; i < size; i++)
            getNames().add(dis.readUTF());
        return this;
    }
}
