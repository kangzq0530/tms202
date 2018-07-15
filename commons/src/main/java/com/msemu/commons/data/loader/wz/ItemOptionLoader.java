package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.ItemOptionDatLoader;
import com.msemu.commons.data.templates.ItemOption;
import com.msemu.commons.data.templates.ItemOptionInfo;
import com.msemu.commons.wz.WzImageProperty;
import com.msemu.commons.wz.WzImage;
import com.msemu.commons.wz.properties.*;
import lombok.Getter;

import java.io.IOException;
import java.util.*;

/**
 * Created by Weber on 2018/4/21.
 */
public class ItemOptionLoader extends WzDataLoader<Map<Integer, ItemOptionInfo>> {

    @Getter
    Map<Integer, ItemOptionInfo> data = new HashMap<>();

    public ItemOptionLoader(WzManager wzManager) {
        super(wzManager);
    }

    @Override
    public void load() {
        WzImage itemOptImg = wzManager.getWz("Item.wz").getWzDirectory().getImage("ItemOption.img");

        itemOptImg.getProperties().forEach(prop -> {
            ItemOptionInfo itemOptionInfo = new ItemOptionInfo();

            WzSubProperty optProp = (WzSubProperty) prop;
            WzSubProperty infoProp = (WzSubProperty) optProp.getFromPath("info");
            WzSubProperty levelProp = (WzSubProperty) optProp.getFromPath("level");

            infoProp.getProperties().forEach(infoSub -> {
                switch (infoSub.getName()) {
                    case "optionType":
                        itemOptionInfo.setOptionType(infoSub.getInt());
                        break;
                    case "reqLevel":
                        itemOptionInfo.setReqLevel(infoSub.getInt());
                        break;
                    case "string":
                        itemOptionInfo.setString(infoSub.getString());
                }
            });

            itemOptionInfo.setId(Integer.parseInt(optProp.getName()));

            levelProp.getProperties().stream().map(p -> (WzSubProperty) p).forEach(p -> {
                ItemOption option = new ItemOption();
                final int level = Integer.parseInt(p.getName());
                option.setLevel(level);

                WzStringProperty faceProp = (WzStringProperty) p.getFromPath("face");
                if (faceProp != null)
                    option.setFace(faceProp.getString());
                Arrays.stream(ItemOption.getTypes()).forEach(type -> {
                    WzImageProperty other = p.getFromPath(type.name());
                    if (other != null)
                        option.setLevelData(type, other.getInt());
                });
                option.setString(itemOptionInfo.getString());
                option.setReqLevel(itemOptionInfo.getReqLevel());
                option.setOptionType(itemOptionInfo.getOptionType());
                itemOptionInfo.getOptions().add(option);
            });
            data.put(itemOptionInfo.getId(), itemOptionInfo);
        });
    }

    @Override
    public void saveToDat() throws IOException {
        if(getData().isEmpty()) {
            load();
        }
        new ItemOptionDatLoader().saveDat(getData());
    }
}
