package com.msemu.commons.data.provider.interfaces;

/**
 * Created by Weber on 2018/3/16.
 */
public interface MapleDataEntry extends MapleDataEntity {

    @Override
    public String getName();

    public int getSize();

    public int getChecksum();

    public int getOffset();
}

