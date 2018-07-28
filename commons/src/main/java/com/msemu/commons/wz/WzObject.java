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

import lombok.Getter;
import lombok.Setter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Weber on 2018/4/20.
 */
public abstract class WzObject implements IDisposable {

    @Getter
    @Setter
    protected Object tag = null;

    @Getter
    @Setter
    protected Object tag2 = null;

    @Getter
    @Setter
    protected Object tag3 = null;

    @Getter
    @Setter
    protected WzObject parent = null;

    @Getter
    @Setter
    protected String name = null;

    @Setter
    @Getter
    private IDataProvider provider;

    public WzObject(String name) {
        this.name = name;
    }

    @Override
    public abstract void dispose();

    public abstract WzObjectType type();

    public void parse() {
        throw new NotImplementedException();
    }

    public String fullPath() {
        if (this instanceof WzFile) {
            return ((WzFile) this).getName();
        }
        String path = getName();
        WzObject currentObject = this;
        while (currentObject.getParent() != null) {
            currentObject = currentObject.getParent();
            path = currentObject.getName() + "/" + path;
        }
        return path;
    }

    public String getString() {
        return getName();
    }

    public long getLong() {
        throw new NotImplementedException();
    }

    public int getInt() {
        throw new NotImplementedException();
    }

    public short getShort() {
        throw new NotImplementedException();
    }

    public Point getPoint() {
        throw new NotImplementedException();
    }

    public float getFloat() {
        throw new NotImplementedException();
    }

    public double getDouble() {
        throw new NotImplementedException();
    }

    public BufferedImage getBitmap() {
        throw new NotImplementedException();
    }

    public byte[] getBytes() {
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        return getString();
    }


}
