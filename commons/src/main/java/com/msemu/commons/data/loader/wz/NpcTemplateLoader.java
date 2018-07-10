package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.NpcTemplateDatLoader;
import com.msemu.commons.data.templates.NpcTemplate;
import com.msemu.commons.wz.WzFile;
import com.msemu.commons.wz.WzImage;
import com.msemu.commons.wz.properties.WzStringProperty;
import com.msemu.commons.wz.properties.WzSubProperty;
import lombok.Getter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Weber on 2018/4/25.
 */
public class NpcTemplateLoader extends WzDataLoader<Set<NpcTemplate>> {

    @Getter
    Set<NpcTemplate> data = new HashSet<>();

    public NpcTemplateLoader(WzManager wzManager) {
        super(wzManager);
    }

    @Override
    public void load() {
        WzFile npcWZ = wzManager.getWz(WzManager.NPC);
        WzImage npcStrImg = wzManager.getWz(WzManager.STRING).getWzDirectory().getImage("Npc.img");

        npcWZ.getWzDirectory().getImages().forEach(img -> data.add(importNpcImg(img)));
        importStrings(data, npcStrImg);

    }

    private void importStrings(Set<NpcTemplate> data, WzImage npcStrImg) {

        data.forEach(nt -> {
            if (npcStrImg.getFromPath(String.format("%d/name", nt.getId())) != null) {
                nt.setName(npcStrImg.getFromPath(String.format("%d/name", nt.getId())).getString());
            }
        });
    }

    private NpcTemplate importNpcImg(WzImage img) {
        NpcTemplate nt = new NpcTemplate();
        int id = Integer.parseInt(img.getName().replace(".img", ""));
        nt.setId(id);
        if (img.getFromPath("info/script") != null) {
            if (img.getFromPath("info/script") instanceof WzSubProperty) {
                WzSubProperty scriptsProp = (WzSubProperty) img.getFromPath("info/script");
                scriptsProp.getProperties().stream().forEach(each -> {
                    if (each.getFromPath("script") != null) {
                        nt.setScript(each.getFromPath("script").getString());
                    }
                    if(each instanceof WzStringProperty) {
                        nt.setScript(each.getString());
                    }
                });
            } else {
                nt.setScript(img.getFromPath("info/script").getString());
            }
        }
        return nt;
    }

    @Override
    public void saveToDat() throws IOException {
        if(getData().isEmpty())
            load();
        new NpcTemplateDatLoader().saveDat(getData());
    }
}
