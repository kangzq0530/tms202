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
