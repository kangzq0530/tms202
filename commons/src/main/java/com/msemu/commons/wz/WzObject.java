package com.msemu.commons.wz;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Weber on 2018/4/20.
 */
public abstract class WzObject implements IDisposable {

    @Getter
    @Setter
    protected Object tag = null;

    @Getter
    @Setter
    protected Object tag2 = null;

    @Getter
    @Setter
    protected Object tag3 = null;

    @Getter
    @Setter
    protected WzObject parent = null;

    @Getter
    @Setter
    protected String name = null;

    @Setter
    @Getter
    private IDataProvider provider;

    public WzObject(String name) {
        this.name = name;
    }

    @Override
    public abstract void dispose();

    public abstract WzObjectType type();

    public void parse() {
        throw new NotImplementedException();
    }

    public String fullPath() {
        if( this instanceof  WzFile) {
            return ((WzFile)this).getName();
        }
        String path = getName();
        WzObject currentObject = this;
        while (currentObject.getParent() != null) {
            currentObject = currentObject.getParent();
            path = currentObject.getName() + "/" + path;
        }
        return path;
    }

    public String getString() {
        return getName();
    }

    public long getLong() {
        throw new NotImplementedException();
    }

    public int getInt() {
        throw new NotImplementedException();
    }

    public short getShort() {
        throw new NotImplementedException();
    }

    public Point getPoint() {
        throw new NotImplementedException();
    }

    public float getFloat() {
        throw new NotImplementedException();
    }

    public double getDouble() {
        throw new NotImplementedException();
    }

    public BufferedImage getBitmap() {
        throw new NotImplementedException();
    }

    public byte[] getBytes() {
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        return getString();
    }


}
