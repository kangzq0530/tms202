package com.msemu.commons.data.provider;

import com.msemu.commons.data.provider.interfaces.MapleCanvas;
import com.msemu.commons.data.provider.interfaces.MapleData;
import com.msemu.commons.data.provider.interfaces.MapleDataEntity;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Weber on 2018/3/21.
 */
public class MapleDataTool {

    public static String getString(MapleData source) {
        return source.getData().toString();
    }

    public static String getString(MapleData source, String def) {
        if (source == null || source.getData() == null) {
            return def;
        } else {
            return getString(source);
        }
    }

    public static String getString(String path, MapleData source) {
        return getString(source.getChildByPath(path));
    }

    public static String getString(String path, MapleData data, String def) {
        return getString(data.getChildByPath(path), def);
    }

    public static double getDouble(MapleData data) {
        return ((Double) data.getData());
    }

    public static float getFloat(MapleData data) {
        return ((Float) data.getData());
    }

    public static float getFloat(MapleData source, float def) {
        if (source == null || source.getData() == null) {
            return def;
        } else {
            return getFloat(source);
        }
    }

    public static int getInt(MapleData source) {
        if (null == source.getType()) {
            return ((Integer) source.getData());
        } else switch (source.getType()) {
            case STRING:
                return Integer.parseInt(getString(source));
            case SHORT:
                return Integer.valueOf(((Short) source.getData()));
            default:
                return ((Integer) source.getData());
        }
    }

    public static int getInt(MapleData source, int def) {
        if (source == null || source.getData() == null)
            return def;
        return getInt(source);
    }

    public static int getInt(String path, MapleData source) {
        return getInt(source.getChildByPath(path));
    }

    public static int getInt(String path, MapleData source, int def) {
        return getInt(source.getChildByPath(path), def);
    }

    public static boolean getBoolean(MapleData source) {
        return getInt(source) > 0;
    }

    public static boolean getBoolean(MapleData source, boolean def) {
        if (source == null || source.getData() == null)
            return def;
        return getInt(source) > 0;
    }

    public static boolean getBoolean(String path, MapleData source) {
        return getBoolean(source.getChildByPath(path));
    }

    public static boolean getBoolean(String path, MapleData source, boolean def) {
        return getBoolean(source.getChildByPath(path), def);
    }


    public static BufferedImage getImage(MapleData source) {
        return ((MapleCanvas) source.getData()).getImage();
    }

    public static Point getPoint(MapleData source) {
        return ((Point) source.getData());
    }

    public static Point getPoint(String path, MapleData source) {
        return getPoint(source.getChildByPath(path));
    }

    public static Point getPoint(String path, MapleData data, Point def) {
        final MapleData pointData = data.getChildByPath(path);
        if (pointData == null) {
            return def;
        }
        return getPoint(pointData);
    }

    public static String getFullDataPath(MapleData source) {
        String path = "";
        MapleDataEntity myData = source;
        while (myData != null) {
            path = myData.getName() + "/" + path;
            myData = myData.getParent();
        }
        return path.substring(0, path.length() - 1);
    }
}
