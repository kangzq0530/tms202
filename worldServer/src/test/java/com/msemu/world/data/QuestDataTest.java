package com.msemu.world.data;

import com.msemu.commons.wz.WzManager;
import com.msemu.core.configs.CoreConfig;
import com.msemu.world.client.character.quest.Quest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Weber on 2018/4/23.
 */
public class QuestDataTest {

    @Test
    public void testLoad() {
        CoreConfig.WZ_PATH = "F:/repos/ZZMS_dev/wz";
        WzManager wzManager = WorldWzManager.getInstance();
        QuestData.getInstance();
    }

}