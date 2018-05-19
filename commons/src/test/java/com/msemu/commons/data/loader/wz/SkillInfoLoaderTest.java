package com.msemu.commons.data.loader.wz;

import com.msemu.core.configs.CoreConfig;
import org.junit.Test;

/**
 * Created by Weber on 2018/4/23.
 */
public class SkillInfoLoaderTest {

    @Test
    public void testLoadSkillInfo() {

        CoreConfig.WZ_PATH = "F:/repos/ZZMS_dev/wz";
        WzManager wzManager = new WzManager(WzManager.SKILL);
        new SkillInfoLoader().load(wzManager);


    }

}