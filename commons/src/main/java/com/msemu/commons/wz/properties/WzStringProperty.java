package com.msemu.commons.wz.properties;

import com.msemu.commons.wz.*;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzStringProperty extends WzImageProperty {

    private String value;

    public WzStringProperty(String name) {
        super(name);
    }

    @Override
    public WzPropertyType propType() {
        return WzPropertyType.String;
    }

    @Override
    public WzObjectType type() {
        return WzObjectType.Property;
    }

    @Override
    public void setValue(Object value) {
        this.value = (String) value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String getString() {
        return toString();
    }

    @Override
    public int getInt() {
        return Integer.parseInt(value);
    }

    @Override
    public short getShort() {
        return Short.parseShort(value);
    }

    @Override
    public double getDouble() {
        return Double.parseDouble(value);
    }

    @Override
    public float getFloat() {
        return Float.parseFloat(value);
    }

    @Override
    public long getLong() {
        return Long.parseLong(value);
    }
}
