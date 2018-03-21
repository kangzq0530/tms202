package com.msemu.commons.data.provider;

import com.msemu.commons.data.provider.interfaces.MapleDataProvider;
import com.msemu.commons.data.provider.wz.xml.XMLDataProvider;
import com.msemu.core.configs.CoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Weber on 2018/3/16.
 */
public class MapleDataProviderFactory {

    private static final Logger log = LoggerFactory.getLogger(MapleDataProviderFactory.class);

    private static final HashMap<String, MapleDataProvider> caches = new HashMap<>();

    private static MapleDataProvider getProvider(File file) {
        String fileName = file.getName();
        String extension = "";
        int extIndex = fileName.lastIndexOf('.');
        if (extIndex > 0) {
            extension = fileName.substring(extIndex + 1);
        }
        if (file.isDirectory() && extension.equals("wz")) {
            if(!caches.containsKey(fileName))
                caches.put(fileName, new XMLDataProvider(file));
            return caches.get(fileName);
        }
        throw new RuntimeException("Can't not find the right MapleDataProvider");
    }


    public static MapleDataProvider getDataProvider(String filename) {
        return getProvider(new File(CoreConfig.WZ_PATH, filename));
    }
}
