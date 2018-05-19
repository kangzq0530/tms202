package com.msemu.commons.wz.properties;

import com.msemu.commons.utils.types.Rect;
import com.msemu.commons.wz.WzExtended;
import com.msemu.commons.wz.WzPropertyType;

import java.awt.*;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzVectorProperty extends WzExtended {

    private WzIntProperty x, y;

    public WzVectorProperty(String name) {
        super(name);
        x = new WzIntProperty("x");
        y = new WzIntProperty("y");
    }

    @Override
    public WzPropertyType propType() {
        return WzPropertyType.Vector;
    }

    @Override
    public void setValue(Object object) {
        if (object instanceof Point) {
            x.setValue(((Point) object).x);
            y.setValue(((Point) object).y);
        } else if (object instanceof Rect) {
            x.setValue(((Rect) object).getWidth());
            y.setValue(((Rect) object).getHeight());
        }
    }

    @Override
    public Point getPoint() {
        return new Point(x.getInt(), y.getInt());
    }

    @Override
    public String toString() {
        return "X: " + x.getString() + ", Y: " + y.getString();
    }
}
