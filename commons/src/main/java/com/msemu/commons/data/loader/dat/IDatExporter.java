package com.msemu.commons.data.loader.dat;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/27.
 */
public interface IDatExporter<T> {
    void saveDat(T data) throws IOException;
}
