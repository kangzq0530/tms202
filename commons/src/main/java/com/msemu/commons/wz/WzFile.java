package com.msemu.commons.wz;

import lombok.Getter;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzFile extends WzObject {


    @Getter
    private WzDirectory wzDirectory;

    @Getter
    private WzHeader header;

    public WzFile(String name) {
        super(name);
        wzDirectory = new WzDirectory(name);
        wzDirectory.setParent(this);
    }


    @Override
    public void dispose() {
        if (wzDirectory == null)
            return;
        wzDirectory.dispose();
        wzDirectory = null;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public WzObjectType type() {
        return WzObjectType.File;
    }


}
