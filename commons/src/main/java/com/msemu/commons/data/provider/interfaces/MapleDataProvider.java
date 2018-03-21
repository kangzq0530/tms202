package com.msemu.commons.data.provider.interfaces;

/**
 * Created by Weber on 2018/3/16.
 */
public interface MapleDataProvider {

    MapleData getData(String path);

    MapleDataDirectoryEntry getRoot();
}
