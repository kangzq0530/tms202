package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.templates.NpcTemplate;
import com.msemu.core.configs.CoreConfig;
import org.junit.Test;

import java.util.Set;

/**
 * Created by Weber on 2018/4/25.
 */
public class NpcTemplateLoaderTest {
    @Test
    public void testLoad() {
        CoreConfig.WZ_PATH = "F:/repos/ZZMS_Dev/wz";
        WzManager wzManager = new WzManager(WzManager.NPC);
        Set<NpcTemplate> templates = new NpcTemplateLoader().load(wzManager);
    }
}