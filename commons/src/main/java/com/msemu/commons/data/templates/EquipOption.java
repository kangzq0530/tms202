package com.msemu.commons.data.templates;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/21.
 */
@Getter
@Setter
public class EquipOption implements DatSerializable{
    private int option;
    private int level;

    public EquipOption() {
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(this.level);
        dos.writeInt(this.option);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        this.setLevel(dis.readInt());
        this.setOption(dis.readInt());
        return this;
    }
}