package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.templates.field.FieldTemplate;
import com.msemu.core.configs.CoreConfig;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Weber on 2018/4/24.
 */
public class FieldLoaderTest {

    @Test
    public void testLoad() {
        CoreConfig.WZ_PATH = "F:/repos/ZZMS_Dev/wz";
        WzManager wzManager = new WzManager(WzManager.MAP, WzManager.MAP2);
        new FieldTemplateLoader(wzManager).load();
    }
}