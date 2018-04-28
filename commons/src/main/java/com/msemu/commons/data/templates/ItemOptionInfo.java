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
public class ItemOptionInfo implements DatSerializable {
    @Getter
    private List<ItemOption> options = new ArrayList<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(options.size());
        for(ItemOption option: options) {
            option.write(dos);
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        for(int i = 0 ; i < size; i++) {
            getOptions().add((ItemOption) new ItemOption().load(dis));
        }
        return this;
    }
}
