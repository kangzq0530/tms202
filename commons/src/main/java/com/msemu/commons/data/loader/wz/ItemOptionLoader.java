package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.ItemOptionDatLoader;
import com.msemu.commons.data.templates.ItemOption;
import com.msemu.commons.data.templates.ItemOptionInfo;
import com.msemu.commons.wz.WzImageProperty;
import com.msemu.commons.wz.WzImage;
import com.msemu.commons.wz.properties.*;

import java.io.IOException;
import java.util.*;

/**
 * Created by Weber on 2018/4/21.
 */
public class ItemOptionLoader extends WzDataLoader<Map<Integer, ItemOptionInfo>> {
    @Override
    public Map<Integer, ItemOptionInfo> load(WzManager wzManager) {

        Map<Integer, ItemOptionInfo> data = new HashMap<>();

        WzImage itemOptImg = wzManager.getWz("Item.wz").getWzDirectory().getImage("ItemOption.img");

        itemOptImg.getProperties().forEach(prop -> {
            ItemOptionInfo levelData = new ItemOptionInfo();

            WzSubProperty optProp = (WzSubProperty) prop;
            WzSubProperty info = (WzSubProperty) optProp.getFromPath("info");
            WzSubProperty level = (WzSubProperty) optProp.getFromPath("level");

            level.getProperties().stream().map(p -> (WzSubProperty) p).forEach(p -> {
                ItemOption option = new ItemOption();
                option.setId(optProp.getInt());
                info.getProperties().forEach(infoSub -> {
                    switch (infoSub.getName()) {
                        case "optionType":
                            option.setOptionType(infoSub.getInt());
                            break;
                        case "reqLevel":
                            option.setReqLevel(infoSub.getInt());
                            break;
                    }
                });
                WzStringProperty faceProp = (WzStringProperty) p.getFromPath("face");
                if (faceProp != null)
                    option.setFace(faceProp.getString());

                Arrays.stream(ItemOption.getTypes()).forEach(type -> {
                    WzImageProperty other = p.getFromPath(type.name());
                    if (other != null)
                        option.setLevelData(type, other.getInt());
                });
                levelData.getOptions().add(option);
            });
            data.put(optProp.getInt(), levelData);
        });

        return data;

    }

    @Override
    public void saveToDat(WzManager wzManager) throws IOException {
        new ItemOptionDatLoader().saveDat(load(wzManager));
    }
}
