package com.msemu.commons.wz.properties;

import com.msemu.commons.wz.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzCanvasProperty extends WzExtended implements IPropertyContainer {

    WzPngProperty pngProperty;

    public WzCanvasProperty(String name) {
        super(name);
        pngProperty = new WzPngProperty();
    }

    @Override
    public void setValue(Object object) {
        pngProperty.setValue(object);
    }

    @Override
    public WzPropertyType propType() {
        return WzPropertyType.Canvas;
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
            properties.remove(prop);
            prop.setParent(null);
        }
    }

    @Override
    public void clearProperties() {
        properties.forEach(p -> p.setParent(null));
        properties.clear();
    }

    @Override
    public List<WzImageProperty> getProperties() {
        return properties;
    }

    @Override
    public WzImageProperty get(String name) {
        return properties.stream()
                .filter(p->p.getName().equals(name))
                .findFirst().orElse(null);
    }


    @Override
    public WzObjectType type() {
        return WzObjectType.Property;
    }
}
