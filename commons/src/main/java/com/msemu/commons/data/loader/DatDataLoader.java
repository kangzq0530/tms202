package com.msemu.commons.data.loader;

import com.msemu.commons.data.loader.dat.DatManager;
import com.msemu.commons.data.loader.dat.DatSerializable;
import com.msemu.commons.data.loader.dat.IDatExporter;
import com.msemu.commons.utils.FileUtils;
import com.msemu.core.configs.CoreConfig;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by Weber on 2018/4/27.
 */
public abstract class DatDataLoader<T extends DatSerializable> implements IDataLoader<T> , IDatExporter<T> {

    protected static final Logger log = LoggerFactory.getLogger(DatDataLoader.class);

    protected String datFileName;

    @Getter
    private T data;

    public DatDataLoader(String datFileName) {
        this.datFileName = datFileName;
    }

    protected DataOutputStream getDataOutputStream() throws FileNotFoundException {
        FileUtils.makeDirIfAbsent(CoreConfig.DAT_PATH);
        return new DataOutputStream(new FileOutputStream(CoreConfig.DAT_PATH + "/" + datFileName));
    }

    protected DataInputStream getDataInputStream() throws IOException {
        return new DataInputStream(new FileInputStream(CoreConfig.DAT_PATH + "/" + datFileName));
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void saveDat(T template) throws IOException {
        template.write(getDataOutputStream());
    }

    @Override
    public void load() {
        try {
            this.data = (T) create().load(getDataInputStream());
        } catch (IOException e) {
            log.error("load dat error");
        }

    }

    abstract protected T create();

}
