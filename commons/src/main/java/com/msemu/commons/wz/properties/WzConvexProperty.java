package com.msemu.commons.wz.properties;

import com.msemu.commons.wz.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzConvexProperty extends WzExtended implements IPropertyContainer {
    public WzConvexProperty(String name) {
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
        properties.remove(prop.getName());
        prop.setParent(null);
    }

    @Override
    public void clearProperties() {
        properties.forEach(p->p.setParent(null));
        properties.clear();
    }

    @Override
    public List<WzImageProperty> getProperties() {
        return properties;
    }

    @Override
    public WzPropertyType propType() {
        return null;
    }

    @Override
    public WzObjectType type() {
        return null;
    }

    @Override
    public WzImageProperty get(String name) {
       return properties
               .stream().filter(p->p.getName().equals(name))
               .findFirst().orElse(null);
    }
}
