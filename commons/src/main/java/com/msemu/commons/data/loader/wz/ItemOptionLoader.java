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

package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.ItemOptionDatLoader;
import com.msemu.commons.data.templates.ItemOption;
import com.msemu.commons.data.templates.ItemOptionInfo;
import com.msemu.commons.wz.WzImage;
import com.msemu.commons.wz.WzImageProperty;
import com.msemu.commons.wz.properties.WzStringProperty;
import com.msemu.commons.wz.properties.WzSubProperty;
import lombok.Getter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        if (getData().isEmpty()) {
            load();
        }
        new ItemOptionDatLoader().saveDat(getData());
    }
}
