package com.msemu.commons.data.loader;

import com.msemu.commons.data.loader.dat.DatSerializable;

import java.io.IOException;

/**
 * Created by Weber on 2018/4/28.
 */
public abstract class SingleObjectDatLoader<T extends DatSerializable> extends DatDataLoader<T, T> {
    public SingleObjectDatLoader(String datFileName) {
        super(datFileName);
    }

    @Override
    public void saveDat(T data) throws IOException {
        data.write(getDataOutputStream());
    }

    @Override
    public T load(Void ignore) {
        try {
            return (T) create().load(getDataInputStream());
        } catch (IOException e) {
            log.error("load dat error");
        }
        return null;
    }
}
