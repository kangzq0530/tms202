package com.msemu.commons.data.loader;

import com.msemu.commons.data.loader.dat.DatSerializable;
import com.msemu.commons.data.loader.dat.IDatExporter;
import com.msemu.commons.utils.FileUtils;
import com.msemu.core.configs.CoreConfig;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class LazyDatMappingDataLoader<T extends DatSerializable> extends MappingDataLoader<T> implements IDatExporter<Map<Integer, T>> {

    private static final Logger log = LoggerFactory.getLogger(DatMappingDataLoader.class);

    @Getter
    private String datDirName;

    @Getter
    private HashMap<Integer, T> data = new HashMap<>();

    public LazyDatMappingDataLoader(String datDirName) {
        this.datDirName = datDirName;
    }

    @Override
    public void load() {

    }

    @Override
    public T getItem(Integer index) {
        if (data.containsKey(index)) return data.get(index);
        try {
            DataInputStream dis = getDataInputStream(index);
            int key = dis.readInt();
            T t = (T) create().load(dis);
            data.put(key, t);
            return t;
        } catch (IOException e) {
            log.error("load dat error", e);
        }
        return null;
    }

    @Override
    public void saveDat(Map<Integer, T> data) throws IOException {
        for (Map.Entry<Integer, T> entry : data.entrySet()) {
            DataOutputStream dos = getDataOutputStream(entry.getKey());
            dos.writeInt(entry.getKey());
            entry.getValue().write(dos);
            dos.close();
        }
    }

    protected DataOutputStream getDataOutputStream(int index) throws FileNotFoundException {
        FileUtils.makeDirIfAbsent(CoreConfig.DAT_PATH + "/" + datDirName);
        return new DataOutputStream(new FileOutputStream(CoreConfig.DAT_PATH + "/" + datDirName + "/" + String.valueOf(index) + ".dat"));
    }

    protected DataInputStream getDataInputStream(int index) throws IOException {
        return new DataInputStream(new FileInputStream(CoreConfig.DAT_PATH + "/" + datDirName + "/" + String.valueOf(index) + ".dat"));
    }

}
