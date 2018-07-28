/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

/**
 * Created by Weber on 2018/4/28.
 */
public abstract class DatMappingDataLoader<T extends DatSerializable> extends MappingDataLoader<T> implements IDatExporter<Map<Integer, T>> {

    private static final Logger log = LoggerFactory.getLogger(DatMappingDataLoader.class);

    @Getter
    private String datFileName;

    @Getter
    private HashMap<Integer, T> data = new HashMap<>();

    public DatMappingDataLoader(String datFileName) {
        this.datFileName = datFileName;
    }

    @Override
    public void load() {
        try {
            DataInputStream dis = getDataInputStream();
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                int key = dis.readInt();
                T t = (T) create().load(dis);
                data.put(key, t);
            }
        } catch (IOException e) {
            log.error("load dat error", e);
        }
    }

    @Override
    public T getItem(Integer index) {
        return data.getOrDefault(index, null);
    }

    @Override
    public void saveDat(Map<Integer, T> data) throws IOException {
        DataOutputStream dos = getDataOutputStream();
        dos.writeInt(data.size());
        for (Map.Entry<Integer, T> entry : data.entrySet()) {
            dos.writeInt(entry.getKey());
            entry.getValue().write(dos);
        }
        dos.close();
    }

    protected DataOutputStream getDataOutputStream() throws FileNotFoundException {
        FileUtils.makeDirIfAbsent(CoreConfig.DAT_PATH);
        return new DataOutputStream(new FileOutputStream(CoreConfig.DAT_PATH + "/" + datFileName));
    }

    protected DataInputStream getDataInputStream() throws IOException {
        return new DataInputStream(new FileInputStream(CoreConfig.DAT_PATH + "/" + datFileName));
    }
}
