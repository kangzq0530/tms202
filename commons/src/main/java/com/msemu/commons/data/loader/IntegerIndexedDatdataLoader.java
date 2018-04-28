package com.msemu.commons.data.loader;

import com.msemu.commons.data.loader.dat.DatSerializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/4/28.
 */
public abstract class IntegerIndexedDatDataLoader<T extends DatSerializable> extends DatDataLoader<Map<Integer, T>, T> {
    public IntegerIndexedDatDataLoader(String datFileName) {
        super(datFileName);
    }

    @Override
    public Map<Integer, T> load(Void _) {
        try {
            DataInputStream dis = getDataInputStream();
            Map<Integer, T> data = new HashMap<>();
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                Integer key = dis.readInt();
                T t = (T) create().load(dis);
                data.put(key, t);
            }
            return data;
        } catch (IOException e) {
            log.error("load dat error", e);
        }
        return null;
    }

    @Override
    public void saveDat(Map<Integer, T> data) throws IOException {
        DataOutputStream dos = getDataOutputStream();
        dos.writeInt(data.size());
        for (Map.Entry<Integer, T> entry : data.entrySet()) {
            dos.writeInt(entry.getKey());
            entry.getValue().write(dos);
        }
    }
}
