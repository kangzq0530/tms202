package com.msemu.commons.wz;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/20.
 */
public abstract class WzImageProperty extends WzObject implements Cloneable {

    protected List<WzImageProperty> properties = new ArrayList<>();


    public WzImageProperty(String name) {
        super(name);
    }

    @Override
    public void dispose() {
        if( properties == null )
            return;
        properties.forEach(WzImageProperty::dispose);
        properties.clear();
        properties = null;
    }


    public abstract WzPropertyType propType();

    public WzImageProperty getFromPath(String path) {
        String[] segments = path.split("/");
        if (segments[0].equals("..")) {
            String subPath = path.substring(path.indexOf('/') + 1);
            if (parent instanceof WzImage) {
                return ((WzImage) parent).getFromPath(subPath);
            } else if (parent instanceof WzImageProperty) {
                return ((WzImageProperty) parent).getFromPath(subPath);
            } else  {
                return null;
            }
        }
        WzImageProperty ret = null;
        for (String segment : segments) {
            boolean found = false;
            for (WzImageProperty prop : (ret == null ? this.properties : ret.properties)) {
                if (prop.getName().equalsIgnoreCase(segment)) {
                    ret = prop;
                    found = true;
                    break;
                }
            }
            if (!found) {
                return null;
            }
        }
        return ret;
    }

    public void setValue(Object value) {
        throw new NotImplementedException();
    }

    @Override
    public WzObjectType type() {
        return WzObjectType.Property;
    }

    public boolean hasProperty(String name) {
        return getFromPath(name) != null;
    }

}
