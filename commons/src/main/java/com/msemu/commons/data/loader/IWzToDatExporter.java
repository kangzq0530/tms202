package com.msemu.commons.data.loader;

import com.msemu.commons.data.loader.wz.WzManager;

import java.io.IOException;

/**
 * Created by Weber on 2018/4/27.
 */
public interface IWzToDatExporter {
    void saveToDat() throws IOException;
}
