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
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Weber on 2018/4/28.
 */
public abstract class DatSetDataLoader<T extends DatSerializable> extends SetDataLoader<T> implements IDatExporter<Set<T>> {

    private static final Logger log = LoggerFactory.getLogger(DatSetDataLoader.class);

    @Getter
    private String datFileName;

    @Getter
    private Set<T> data = new HashSet<>();


    public DatSetDataLoader(String datFileName) {
        this.datFileName = datFileName;
    }


    @Override
    public void load() {
        try {
            data.clear();
            DataInputStream dis = getDataInputStream();
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                DatSerializable obj = create().load(dis);
                T t = (T) obj;
                data.add(t);
            }
            dis.close();
        } catch (IOException ex) {
            log.error("load dat error", ex);
        }
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

    private DataOutputStream getDataOutputStream() throws FileNotFoundException {
        FileUtils.makeDirIfAbsent(CoreConfig.DAT_PATH);
        return new DataOutputStream(new FileOutputStream(CoreConfig.DAT_PATH + "/" + datFileName));
    }

    private DataInputStream getDataInputStream() throws IOException {
        return new DataInputStream(new FileInputStream(CoreConfig.DAT_PATH + "/" + datFileName));
    }
}
