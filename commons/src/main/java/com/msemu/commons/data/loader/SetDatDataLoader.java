package com.msemu.commons.data.loader;

import com.msemu.commons.data.loader.dat.DatSerializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Weber on 2018/4/28.
 */
public abstract class SetDatDataLoader<T extends DatSerializable> extends DatDataLoader<Set<T>, T> {
    public SetDatDataLoader(String datFileName) {
        super(datFileName);
    }

    @Override
    public Set<T> load(Void src) {
        try {
            DataInputStream dis = getDataInputStream();
            Set<T> data = new HashSet<>();
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {

                DatSerializable obj = create().load(dis);
                T t = (T) obj;
                data.add(t);
            }
            dis.close();
            return data;
        } catch (IOException ex) {
            log.error("load dat error", ex);
        }
        return null;
    }

    @Override
    public void saveDat(Set<T> data) throws IOException {
        DataOutputStream dos = getDataOutputStream();
        dos.writeInt(data.size());
        for (T t : data) {
            t.write(dos);
        }
        dos.close();
    }
}
