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
public class CarnivalReward implements DatSerializable{
    float climax;
    private List<Integer> cpDiff = new ArrayList<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeFloat(climax);
        dos.writeInt(cpDiff.size());
        for(Integer v : cpDiff)
            dos.writeInt(v);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setClimax(dis.readFloat());
        int size = dis.readInt();
        for(int i = 0 ; i < size; i++)
            getCpDiff().add(dis.readInt());
        return this;
    }
}
