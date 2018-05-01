package com.msemu.commons.tools;

import com.msemu.commons.config.utils.ConfigLoader;
import com.msemu.commons.data.loader.dat.ItemOptionDatLoader;
import com.msemu.commons.data.loader.wz.*;
import com.msemu.commons.data.templates.ItemOption;
import com.msemu.commons.data.templates.ItemTemplate;

import java.io.IOException;

/**
 * Created by Weber on 2018/4/28.
 */
public class wzToDat {

    public static void main(String[] args) throws IOException {
        ConfigLoader.getInstance().reload();
        WzManager wzManager = new WzManager();
//        new ForbiddenNameLoader().saveToDat(wzManager);
//        new ItemOptionLoader().saveToDat(wzManager);
//        new ItemOptionDatLoader().load(null);
//        new ItemTemplateLoader().saveToDat(wzManager);
//        new EquipTemplateLoader().saveToDat(wzManager);
//        new FieldTemplateLoader().saveToDat(wzManager);
//        new NpcTemplateLoader().saveToDat(wzManager);
//
//        new SkillInfoLoader().saveToDat(wzManager);
        new QuestInfoLoader().saveToDat(wzManager);
//        new MobTemplateLoader().saveToDat(wzManager);
//        new FieldTemplateLoader().saveToDat(wzManager);

    }
}
