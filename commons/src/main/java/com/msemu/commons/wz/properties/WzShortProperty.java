package com.msemu.commons.wz.properties;

import com.msemu.commons.wz.WzImageProperty;
import com.msemu.commons.wz.WzPropertyType;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzShortProperty extends WzImageProperty {

    private short value;

    public WzShortProperty(String name) {
        super(name);
    }


    @Override
    public WzPropertyType propType() {
        return WzPropertyType.Short;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public double getDouble() {
        return (double) value;
    }

    @Override
    public float getFloat() {
        return (float) value;
    }

    @Override
    public short getShort() {
        return value;
    }

    @Override
    public int getInt() {
        return (int) value;
    }

    @Override
    public long getLong() {
        return (long) value;
    }

    @Override
    public String getString() {
        return toString();
    }


    @Override
    public void setValue(Object object) {
        this.value = (short) value;
    }
}
