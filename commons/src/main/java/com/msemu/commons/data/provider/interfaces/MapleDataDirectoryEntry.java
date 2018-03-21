package com.msemu.commons.data.provider.interfaces;

import java.util.List;

/**
 * Created by Weber on 2018/3/16.
 */
public interface MapleDataDirectoryEntry extends MapleDataEntry {

    List<MapleDataDirectoryEntry> getSubdirectories();

    List<MapleDataFileEntry> getFiles();

    MapleDataEntry getEntry(String name);
}