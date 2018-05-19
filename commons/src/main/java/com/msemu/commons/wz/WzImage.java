package com.msemu.commons.wz;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzImage extends WzObject implements IPropertyContainer {


    private List<WzImageProperty> properties = new ArrayList<>();

    public WzImage(String name) {
        super(name);
    }

    @Override
    public void dispose() {
        if(properties == null)
            return;
        properties.forEach(WzImageProperty::dispose);
        properties.clear();
        properties = null;
    }

    @Override
    public WzObjectType type() {
        return null;
    }

    public WzImageProperty getFromPath(String path) {
        String[] segments = path.split("/");
        if (segments[0].equals("..")) {
            return null;
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

    @Override
    public void addProperty(WzImageProperty prop) {
        prop.setParent(this);
        properties.add(prop);
    }


    @Override
    public void addProperties(List<WzImageProperty> props) {
        props.forEach(this::addProperty);
    }

    @Override
    public void removeProperty(WzImageProperty prop) {
        if (properties.contains(prop)) {
            properties.remove(prop);
        }
    }

    @Override
    public void clearProperties() {
        properties.forEach(p -> p.setParent(null));
        properties.clear();
    }

    @Override
    public List<WzImageProperty> getProperties() {
        return properties.stream().collect(Collectors.toList());
    }

    @Override
    public WzImageProperty get(String name) {
        return properties
                .stream().filter(p -> p.getName().equals(name))
                .findFirst().orElse(null);
    }
}
