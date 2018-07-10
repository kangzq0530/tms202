package com.msemu.commons.data.loader;

import com.msemu.commons.data.loader.dat.DatSerializable;
import com.msemu.commons.data.loader.dat.IDatExporter;
import com.msemu.commons.utils.FileUtils;
import com.msemu.core.configs.CoreConfig;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/28.
 */
public abstract class DatListDataLoader<T extends DatSerializable> extends ListDataLoader<T> implements IDatExporter<List<T>> {

    protected static final Logger log = LoggerFactory.getLogger(DatDataLoader.class);

    private String datFileName;

    protected abstract T create();

    private DataOutputStream getDataOutputStream() throws FileNotFoundException {
        FileUtils.makeDirIfAbsent(CoreConfig.DAT_PATH);
        return new DataOutputStream(new FileOutputStream(CoreConfig.DAT_PATH + "/" + datFileName));
    }

    private DataInputStream getDataInputStream() throws IOException {
        return new DataInputStream(new FileInputStream(CoreConfig.DAT_PATH + "/" + datFileName));
    }

    public DatListDataLoader(String datFileName) {
        this.datFileName = datFileName;
    }

    @Override
    public void load() {
        try {
            data.clear();
            DataInputStream dis = getDataInputStream();
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {

                T t = (T) create().load(dis);
                data.add(t);
            }
            dis.close();
        } catch (IOException ex) {
            log.error("Load data error", ex);
        }
    }

    @Override
    public void saveDat(List<T> data) throws IOException {
        DataOutputStream dos = getDataOutputStream();
        dos.writeInt(data.size());
        for (T i : data) {
            i.write(dos);
        }
        dos.close();
    }

    @Override
    public T getItem(int index) {
        if(index < data.size()) {
            return data.get(index);
        }
        return null;
    }
}
