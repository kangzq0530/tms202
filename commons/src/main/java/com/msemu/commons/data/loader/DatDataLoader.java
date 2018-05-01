package com.msemu.commons.data.loader;

import com.msemu.commons.data.loader.dat.IDatExporter;
import com.msemu.commons.utils.FileUtils;
import com.msemu.core.configs.CoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by Weber on 2018/4/27.
 */
public abstract class DatDataLoader<T, K> implements IDataLoader<T, Void> , IDatExporter<T> {

    protected static final Logger log = LoggerFactory.getLogger(DatDataLoader.class);

    protected String datFileName;

    protected abstract K create();

    protected DataOutputStream getDataOutputStream() throws FileNotFoundException {
        FileUtils.makeDirIfAbsent(CoreConfig.DAT_PATH);
        return new DataOutputStream(new FileOutputStream(CoreConfig.DAT_PATH + "/" + datFileName));
    }

    protected DataInputStream getDataInputStream() throws IOException {
        return new DataInputStream(new FileInputStream(CoreConfig.DAT_PATH + "/" + datFileName));
    }

    public DatDataLoader(String datFileName) {
        this.datFileName = datFileName;
    }

}
