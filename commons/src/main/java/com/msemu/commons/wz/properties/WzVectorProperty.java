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
