/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
        if (properties == null)
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
            } else {
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
