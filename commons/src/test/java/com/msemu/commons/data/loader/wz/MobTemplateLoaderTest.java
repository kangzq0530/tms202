package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.templates.MobTemplate;
import com.msemu.core.configs.CoreConfig;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Weber on 2018/4/25.
 */
public class MobTemplateLoaderTest {

    @Test
    public void testLoad() {
        CoreConfig.WZ_PATH = "F:/repos/ZZMS_Dev/wz";
        WzManager wzManager = new WzManager(WzManager.MOB, WzManager.MOB2);
        new MobTemplateLoader(wzManager).load();
    }
}