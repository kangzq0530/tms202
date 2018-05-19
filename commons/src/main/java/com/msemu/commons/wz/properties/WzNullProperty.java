package com.msemu.commons.wz.properties;

import com.msemu.commons.wz.*;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzNullProperty extends WzImageProperty {
    public WzNullProperty(String name) {
        super(name);
    }

    @Override
    public WzPropertyType propType() {
        return WzPropertyType.Null;
    }

    @Override
    public WzObjectType type() {
        return WzObjectType.Property;
    }
}
