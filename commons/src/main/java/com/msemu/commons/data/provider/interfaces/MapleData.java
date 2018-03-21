package com.msemu.commons.data.provider.interfaces;

import com.msemu.commons.data.provider.MapleDataType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by Weber on 2018/3/16.
 */
public interface MapleData extends MapleDataEntity, Iterable<MapleData> {

    @Override
    String getName();

    MapleDataType getType();

    List<MapleData> getChildren();

    MapleData getChildByPath(String path);

    Object getData();

}
