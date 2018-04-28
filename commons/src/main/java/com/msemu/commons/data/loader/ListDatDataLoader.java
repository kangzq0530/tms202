package com.msemu.commons.data.loader;

import com.msemu.commons.data.loader.dat.DatManager;
import com.msemu.commons.data.loader.dat.DatSerializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/28.
 */
public abstract class ListDatDataLoader<T extends DatSerializable> extends DatDataLoader<List<T>, T> {
    public ListDatDataLoader(String datFileName) {
        super(datFileName);
    }

    public List<T> load(Void source) throws IOException {
        DataInputStream dis = getDataInputStream();
        List<T> data = new ArrayList<T>();
        int size = dis.readInt();
        for(int i = 0 ; i < size;i++) {
            T t = (T) create().load(dis);
            data.add(t);
        }
        dis.close();
        return data;
    }

    @Override
    public void saveDat(List<T> data) throws IOException {
        DataOutputStream dos = getDataOutputStream();
        dos.writeInt(data.size());
        for(T i : data) {
            i.write(dos);
        }
        dos.close();
    }
}
