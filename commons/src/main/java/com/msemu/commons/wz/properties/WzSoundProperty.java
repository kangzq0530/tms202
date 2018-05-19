package com.msemu.commons.wz.properties;

import com.msemu.commons.wz.WzExtended;
import com.msemu.commons.wz.WzObjectType;
import com.msemu.commons.wz.WzPropertyType;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzSoundProperty extends WzExtended {

    public WzSoundProperty(String name) {
        super(name);
    }

    @Override
    public WzPropertyType propType() {
        return WzPropertyType.Sound;
    }

    @Override
    public WzObjectType type() {
        return WzObjectType.Property;
    }
}
