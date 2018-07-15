package com.msemu.commons.tools;

import com.msemu.commons.config.utils.ConfigLoader;
import com.msemu.commons.data.loader.dat.ItemOptionDatLoader;
import com.msemu.commons.data.loader.dat.ItemTemplateDatLoader;
import com.msemu.commons.data.loader.wz.*;

import java.io.IOException;

/**
 * Created by Weber on 2018/4/28.
 */
public class wzToDat {

    public static void main(String[] args) throws IOException {
        ConfigLoader.getInstance().reload();
        WzManager wzManager = new WzManager();
//        new ForbiddenNameLoader(wzManager).saveToDat();
        new ItemOptionLoader(wzManager).saveToDat();
//        new ItemTemplateLoader(wzManager).saveToDat();
//        new EquipTemplateLoader(wzManager).saveToDat();
//        new SetItemInfoLoader(wzManager).saveToDat();
//        new MonsterBookLoader(wzManager).saveToDat();
//        new FieldTemplateLoader(wzManager).saveToDat();
//        new NpcTemplateLoader(wzManager).saveToDat();
//
//        new SkillInfoLoader(wzManager).saveToDat();
//        new QuestInfoLoader(wzManager).saveToDat();
//        new MobTemplateLoader(wzManager).saveToDat();
//        new FieldTemplateLoader(wzManager).saveToDat();
    }
}
