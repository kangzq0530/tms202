package com.msemu.commons.wz.properties;

import com.msemu.commons.wz.WzImageProperty;
import com.msemu.commons.wz.WzPropertyType;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzIntProperty extends WzImageProperty {

    private int value;

    public WzIntProperty(String name) {
        super(name);
    }

    @Override
    public WzPropertyType propType() {
        return WzPropertyType.Short;
    }


    @Override
    public void setValue(Object value) {
        this.value = (int) value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public String getString() {
        return toString();
    }

    @Override
    public long getLong() {
        return (long) value;
    }

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public short getShort() {
        return (short)value;
    }

    @Override
    public float getFloat() {
        return (float)value;
    }

    @Override
    public double getDouble() {
        return (double)value;
    }
}
