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
        if(getData().isEmpty())
            load();
        new MonsterBookDatLoader().saveDat(getData());
    }
}
