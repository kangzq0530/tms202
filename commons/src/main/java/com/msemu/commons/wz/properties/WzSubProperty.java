package com.msemu.commons.wz.properties;

import com.msemu.commons.wz.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzSubProperty extends WzExtended implements IPropertyContainer {
    public WzSubProperty(String name) {
        super(name);
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
        if(properties.contains(prop)) {
            prop.setParent(null);
            properties.remove(prop);
        }
    }

    @Override
    public void clearProperties() {
        properties.forEach(p->p.setParent(null));
        properties.clear();
    }

    @Override
    public List<WzImageProperty> getProperties() {
        return properties.stream().collect(Collectors.toList());
    }

    @Override
    public WzImageProperty get(String name) {
        return properties.stream().filter(p->p.getName().equals(name))
                .findFirst().orElse(null);
    }

    @Override
    public WzPropertyType propType() {
        return WzPropertyType.SubProperty;
    }

    @Override
    public WzObjectType type() {
        return WzObjectType.Property;
    }


    @Override
    public int getInt() {
        return Integer.parseInt(name);
    }

}
