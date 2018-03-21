package com.msemu.world.dataholders.templates;

import com.msemu.commons.data.provider.MapleDataProviderFactory;
import com.msemu.commons.data.provider.interfaces.MapleData;
import com.msemu.commons.data.provider.interfaces.MapleDataDirectoryEntry;
import com.msemu.commons.data.provider.interfaces.MapleDataProvider;
import com.msemu.core.configs.CoreConfig;
import com.msemu.world.model.skills.templates.SkillT;
import junit.framework.TestCase;

/**
 * Created by Weber on 2018/3/21.
 */
public class SkillTTest extends TestCase {

    public void testSkillLoad() {
        CoreConfig.WZ_PATH = "F:\\repos\\ZZMS_dev\\wz";
        final MapleDataProvider skillWZ = MapleDataProviderFactory.getDataProvider("Skill.wz");
        final MapleDataDirectoryEntry root = skillWZ.getRoot();
        root.getFiles()
                .stream()
                .filter(topDir -> topDir.getName().replace(".img", "").matches("^\\d+$"))
                .forEach(topDir -> {
                    for (MapleData subImg : skillWZ.getData(topDir.getName())) {
                        if (subImg.getName().equals("skill")) {
                            for (MapleData skillImg : subImg) {
                                SkillT skillT = new SkillT(skillImg);

                            }
                        }
                    }
                });

    }

}