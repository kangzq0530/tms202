package com.msemu.commons.data.templates.quest;

import com.msemu.commons.data.loader.wz.QuestInfoLoader;
import com.msemu.commons.wz.WzDirectory;
import com.msemu.commons.data.loader.wz.WzManager;
import com.msemu.core.configs.CoreConfig;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestTemplateTest {

    @Test
    public void testLoadQuestWz() {
        CoreConfig.WZ_PATH = "F:/repos/ZZMS_dev/wz";
        WzManager wzManager = new WzManager(WzManager.QUEST);
        WzDirectory wzDirectory = wzManager.getWz(WzManager.QUEST).getWzDirectory();


        new QuestInfoLoader(wzManager).load();
    }
}