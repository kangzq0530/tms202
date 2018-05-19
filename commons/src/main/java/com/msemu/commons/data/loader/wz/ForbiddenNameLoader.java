package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.ForbiddenNameDatLoader;
import com.msemu.commons.data.templates.ForbiddenName;
import com.msemu.commons.wz.WzDirectory;
import com.msemu.commons.wz.WzFile;
import com.msemu.commons.wz.WzImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/21.
 */
public class ForbiddenNameLoader extends WzDataLoader<ForbiddenName> {


    @Override
    public ForbiddenName load(WzManager wzManager) {
        ForbiddenName forbiddenName = new ForbiddenName();
        WzFile wzFile = wzManager.getWz(WzManager.ETC);
        WzDirectory wzDir = wzFile.getWzDirectory();
        WzImage forbiddenNamesImg = wzDir.getImage("ForbiddenName.img");
        forbiddenNamesImg.getProperties().forEach(prop -> forbiddenName.getNames().add(prop.getString()));
        return forbiddenName;
    }

    @Override
    public void saveToDat(WzManager wzManager) throws IOException {
        new ForbiddenNameDatLoader().saveDat(load(wzManager));
    }
}
