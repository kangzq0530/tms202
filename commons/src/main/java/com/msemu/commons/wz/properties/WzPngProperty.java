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

import com.msemu.commons.utils.types.Size;
import com.msemu.commons.wz.WzImageProperty;
import com.msemu.commons.wz.WzObjectType;
import com.msemu.commons.wz.WzPropertyType;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzPngProperty extends WzImageProperty {


    @Getter
    @Setter
    private int width, height, format, format2;

    private BufferedImage value;

    public WzPngProperty() {
        super("PNG");
    }

    @Override
    public void setValue(Object object) {
        if (object instanceof Size) {
            Size rect = (Size) object;
            width = rect.getWidth();
            height = rect.getHeight();
        } else if (object instanceof BufferedImage) {
            BufferedImage image = (BufferedImage) object;
            width = image.getWidth();
            height = image.getHeight();
            value = value;
        }
    }

    @Override
    public WzPropertyType propType() {
        return WzPropertyType.PNG;
    }

    @Override
    public WzObjectType type() {
        return WzObjectType.Property;
    }

}
