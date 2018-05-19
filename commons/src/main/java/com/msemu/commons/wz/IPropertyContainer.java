package com.msemu.commons.wz;

import java.util.List;

/**
 * Created by Weber on 2018/4/20.
 */
public interface IPropertyContainer {

    void addProperty(WzImageProperty prop);

    void addProperties(List<WzImageProperty> props);

    void removeProperty(WzImageProperty prop);

    void clearProperties();

    List<WzImageProperty> getProperties();

    WzImageProperty get(String name);
}
