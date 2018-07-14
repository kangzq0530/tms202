package com.msemu.commons.data.loader;

import com.msemu.commons.data.loader.dat.DatSerializable;
import com.msemu.commons.data.loader.dat.IDatExporter;
import com.msemu.commons.utils.FileUtils;
import com.msemu.core.configs.CoreConfig;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public abstract class LazyDatMappingDataLoader<T extends DatSerializable> extends MappingDataLoader<T> implements IDatExporter<Map<Integer, T>> {

    private static final Logger log = LoggerFactory.getLogger(DatMappingDataLoader.class);


    @Getter
    private Map<Integer, Integer> headers = new HashMap<>();

    @Getter
    private long dataPos;

    @Getter
    private String datFileName;

    @Getter
    private HashMap<Integer, T> data = new HashMap<>();

    public LazyDatMappingDataLoader(String datFileName) {
        this.datFileName = datFileName;
    }

    @Override
    public void load() {
        headers.clear();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(CoreConfig.DAT_PATH + "/" + datFileName));
            final int size = dis.available();
            final int itemCount = dis.readInt();
            for (int i = 0; i < itemCount; i++) {
                headers.put(dis.readInt(), dis.readInt());
            }
            this.dataPos = size - dis.available();
            dis.close();
        } catch (IOException e) {
            log.error("load dat error", e);
        }
    }

    @Override
    public T getItem(Integer index) {
        if (data.containsKey(index))
            return data.get(index);
        if (!headers.containsKey(index))
            return null;
        final long position = dataPos + headers.get(index);
        try {
            try (FileChannel ch = FileChannel.open(Paths.get(CoreConfig.DAT_PATH + "/" + datFileName), StandardOpenOption.READ)) {
                DataInputStream dis = new DataInputStream(Channels.newInputStream(ch.position(position)));
                T t = (T) create().load(dis);
                data.put(index, t);
                return t;
            }
        } catch (IOException e) {
            log.error("load dat error", e);
        }
        return null;
    }

    @Override
    public void saveDat(Map<Integer, T> data) throws IOException {
        FileUtils.makeDirIfAbsent(CoreConfig.DAT_PATH + "/");
        ByteArrayOutputStream headerData = new ByteArrayOutputStream();
        DataOutputStream headerDos = new DataOutputStream(headerData);
        ByteArrayOutputStream bodyData = new ByteArrayOutputStream();
        DataOutputStream bodyDos = new DataOutputStream(bodyData);
        for (Map.Entry<Integer, T> entry : data.entrySet()) {
            int offset = bodyDos.size();
            headerDos.writeInt(entry.getKey());
            headerDos.writeInt(offset);
            entry.getValue().write(bodyDos);
        }
        headerDos.flush();
        bodyDos.flush();
        byte[] header = headerData.toByteArray();
        byte[] body = bodyData.toByteArray();
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(CoreConfig.DAT_PATH  + "/" + datFileName));

        dos.writeInt(data.size());
        dos.write(header);
        dos.write(body);
        dos.close();
        headerDos.close();
        bodyDos.close();
        headerData.close();
        bodyData.close();
    }

    public int size() {
        return headers.size();
    }

}
