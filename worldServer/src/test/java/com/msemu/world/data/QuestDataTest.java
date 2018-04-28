package com.msemu.world.data;

import com.msemu.commons.data.loader.wz.WzManager;
import com.msemu.core.configs.CoreConfig;
import org.junit.Test;

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