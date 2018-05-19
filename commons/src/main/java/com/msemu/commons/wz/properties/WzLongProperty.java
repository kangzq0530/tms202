package com.msemu.commons.wz.properties;


import com.msemu.commons.wz.WzImageProperty;
import com.msemu.commons.wz.WzPropertyType;

/**
 * Created by Weber on 2018/4/21.
 */
public class WzLongProperty extends WzImageProperty {

    private long value;

    public WzLongProperty(String name) {
        super(name);
    }
    @Override
    public void setValue(Object value) {
        this.value = (long) value;
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
        return (int)value;
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

    @Override
    public WzPropertyType propType() {
        return null;
    }
}
