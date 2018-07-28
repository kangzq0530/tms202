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
import com.msemu.commons.data.loader.dat.MonsterBookDatLoader;
import com.msemu.commons.data.templates.MonsterBook;
import com.msemu.commons.wz.WzImage;
import com.msemu.commons.wz.properties.WzSubProperty;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/5/6.
 */
public class MonsterBookLoader extends WzDataLoader<List<MonsterBook>> {

    @Getter
    List<MonsterBook> data = new ArrayList<>();

    public MonsterBookLoader(WzManager wzManager) {
        super(wzManager);
    }

    @Override
    public void load() throws IOException {
        WzImage mbImg = getWzManager().getWz(WzManager.STRING).getWzDirectory().getImage("MonsterBook.img");

        mbImg.getProperties()
                .stream()
                .map(prop -> (WzSubProperty) prop)
                .forEach(mobProp -> {
                    MonsterBook mb = new MonsterBook();
                    data.add(mb);
                    Integer mobTemplateID = Integer.parseInt(mobProp.getName());
                    mb.setMobTemplateID(mobTemplateID);
                    mobProp.getProperties().forEach(prop -> {
                        String propName = prop.getName();
                        if (propName.equalsIgnoreCase("episode")) {
                            mb.setEpisode(prop.getString());
                        } else if (propName.equalsIgnoreCase("map")) {
                            ((WzSubProperty) prop).getProperties()
                                    .forEach(pp -> {
                                        mb.getMaps().add(pp.getInt());
                                    });
                        } else if (propName.equalsIgnoreCase("reward")) {
                            ((WzSubProperty) prop).getProperties()
                                    .forEach(pp -> {
                                        mb.getRewards().add(pp.getInt());
                                    });
                        }
                    });
                });
    }

    @Override
    public void saveToDat() throws IOException {
        if (getData().isEmpty())
            load();
        new MonsterBookDatLoader().saveDat(getData());
    }
}
