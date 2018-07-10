package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.ForbiddenNameDatLoader;
import com.msemu.commons.data.templates.ForbiddenName;
import com.msemu.commons.wz.WzDirectory;
import com.msemu.commons.wz.WzFile;
import com.msemu.commons.wz.WzImage;
import lombok.Getter;

import java.io.IOException;

/**
 * Created by Weber on 2018/4/21.
 */
public class ForbiddenNameLoader extends WzDataLoader<ForbiddenName> {

    @Getter
    ForbiddenName data = null;

    public ForbiddenNameLoader(WzManager wzManager) {
        super(wzManager);
    }

    @Override
    public void load() {
        data = new ForbiddenName();
        WzFile wzFile = wzManager.getWz(WzManager.ETC);
        WzDirectory wzDir = wzFile.getWzDirectory();
        WzImage forbiddenNamesImg = wzDir.getImage("ForbiddenName.img");
        forbiddenNamesImg.getProperties().forEach(prop -> data.getNames().add(prop.getString()));
    }

    @Override
    public void saveToDat() throws IOException {
        if (getData() == null) {
            load();
        }
        new ForbiddenNameDatLoader().saveDat(getData());
    }
}
