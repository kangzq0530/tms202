package com.msemu.commons.wz.properties;

import com.msemu.commons.wz.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzUOLProperty extends WzExtended {

    private WzObject linkObject;

    private String value;

    public WzUOLProperty(String name) {
        super(name);
    }

    @Override
    public void dispose() {
        if (linkObject != null)
            linkObject.dispose();
        linkObject = null;
    }

    @Override
    public WzPropertyType propType() {
        return WzPropertyType.UOL;
    }


    @Override
    public String toString() {
        return linkObject.toString();
    }

    @Override
    public String getString() {
        return linkObject.getString();
    }

    @Override
    public long getLong() {
        return super.getLong();
    }

    @Override
    public int getInt() {
        return linkObject.getInt();
    }

    @Override
    public short getShort() {
        return linkObject.getShort();
    }

    @Override
    public Point getPoint() {
        return linkObject.getPoint();
    }

    @Override
    public float getFloat() {
        return linkObject.getFloat();
    }

    @Override
    public double getDouble() {
        return linkObject.getDouble();
    }

    @Override
    public BufferedImage getBitmap() {
        return linkObject.getBitmap();
    }

    @Override
    public byte[] getBytes() {
        return linkObject.getBytes();
    }

    @Override
    public void setValue(Object value) {
        this.value = (String) value;
    }

    private WzObject getLinkObject() {
        if (linkObject == null) {
            String[] segments = value.split("/");
            linkObject = parent;
            for (String path : segments) {
                if (path.equalsIgnoreCase("..")) {
                    linkObject = linkObject.getParent();
                } else {
                    if (linkObject instanceof WzImageProperty) {
                        linkObject = ((WzImageProperty) linkObject).getFromPath(path);
                    } else if (linkObject instanceof WzImage) {
                        linkObject = ((WzImage) linkObject).getFromPath(path);
                    } else if (linkObject instanceof WzDirectory) {
                        linkObject = ((WzDirectory) linkObject).get(path);
                    } else {
                        throw new RuntimeException("UOL error");
                    }
                }
            }
        }
        return linkObject;
    }
}
