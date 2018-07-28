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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzDirectory extends WzObject {

    private List<WzImage> images = new ArrayList<>();

    private List<WzDirectory> dirs = new ArrayList<>();

    public WzDirectory(String name) {
        super(name);
    }

    @Override
    public void dispose() {
        if (images != null) {
            images.forEach(WzImage::dispose);
            images.clear();
        }
        if (dirs != null) {
            dirs.forEach(WzDirectory::dispose);
            dirs.clear();
        }
        images = null;
        dirs = null;
    }

    @Override
    public WzObjectType type() {
        return WzObjectType.Directory;
    }


    public WzObject get(String name) {
        WzObject obj = images.stream()
                .filter(img -> img.getName().equals(name))
                .findFirst().orElse(null);
        if (obj == null)
            dirs.stream().filter(dir -> dir.getName().equals(name))
                    .findFirst().orElse(null);
        return obj;
    }

    public void addImage(WzImage wzImage) {
        wzImage.setParent(this);
        images.add(wzImage);
    }

    public void addDirectory(WzDirectory wzDir) {
        wzDir.setParent(this);
        dirs.add(wzDir);
    }

    public WzDirectory getDir(String name) {
        return dirs.stream().filter(dir -> dir.getName().equals(name))
                .findFirst().orElse(null);
    }


    public WzImage getImage(String name) {
        return images.stream()
                .filter(img -> img.getName().equals(name))
                .findFirst().orElse(null);
    }

    public List<WzImage> getImages() {
        return images;
    }

    public List<WzDirectory> getDirs() {
        return dirs;
    }

}
