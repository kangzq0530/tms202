package com.msemu.commons.wz.properties;

import com.msemu.commons.wz.WzImageProperty;
import com.msemu.commons.wz.WzObjectType;
import com.msemu.commons.wz.WzPropertyType;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzFloatProperty extends WzImageProperty {

    private float value;

    public WzFloatProperty(String name) {
        super(name);
    }

    @Override
    public WzPropertyType propType() {
        return WzPropertyType.Float;
    }

    @Override
    public WzObjectType type() {
        return WzObjectType.Property;
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
        return value;
    }

    @Override
    public double getDouble() {
        return (double)value;
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
    public void setValue(Object value) {
        this.value = (float)value;
    }
}
