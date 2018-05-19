package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.VCoreDatLoader;
import com.msemu.commons.data.templates.skill.VCoreEnforcementEntry;
import com.msemu.commons.data.templates.skill.VCoreInfo;
import com.msemu.commons.wz.WzFile;
import com.msemu.commons.wz.WzImage;
import com.msemu.commons.wz.properties.WzSubProperty;

import java.io.IOException;

/**
 * Created by Weber on 2018/5/7.
 */
public class VCoreInfoLoader extends WzDataLoader<VCoreInfo> {

    @Override
    public VCoreInfo load(WzManager source) throws IOException {
        VCoreInfo vCoreInfo = new VCoreInfo();

        WzFile etc = source.getWz(WzManager.ETC);
        WzImage vCoreImg = etc.getWzDirectory().getImage("VCore.img");

        WzSubProperty skillProp = (WzSubProperty) vCoreImg.getFromPath("Enforcement/Skill");
        WzSubProperty enforceProp = (WzSubProperty) vCoreImg.getFromPath("Enforcement");
        WzSubProperty specialProp = (WzSubProperty) vCoreImg.getFromPath("Enforcement");
        skillProp.getProperties().forEach(prop -> {
            int level = Integer.parseInt(prop.getName());
            int nextExp = prop.getFromPath("nextExp").getInt();
            int expEnforce = prop.getFromPath("expEnforce").getInt();
            int extract = prop.getFromPath("extract").getInt();
            VCoreEnforcementEntry entry = new VCoreEnforcementEntry(level, nextExp, expEnforce, extract);
            vCoreInfo.addSkill(level, entry);
        });
        enforceProp.getProperties().forEach(prop -> {
            int level = Integer.parseInt(prop.getName());
            int nextExp = prop.getFromPath("nextExp").getInt();
            int expEnforce = prop.getFromPath("expEnforce").getInt();
            int extract = prop.getFromPath("extract").getInt();
            VCoreEnforcementEntry entry = new VCoreEnforcementEntry(level, nextExp, expEnforce, extract);
            vCoreInfo.addEnhance(level, entry);
        });
        specialProp.getProperties().forEach(prop -> {
            int level = Integer.parseInt(prop.getName());
            int nextExp = prop.getFromPath("nextExp").getInt();
            int expEnforce = prop.getFromPath("expEnforce").getInt();
            int extract = prop.getFromPath("extract").getInt();
            VCoreEnforcementEntry entry = new VCoreEnforcementEntry(level, nextExp, expEnforce, extract);
            vCoreInfo.addSpecial(level, entry);
        });
        return vCoreInfo;
    }

    @Override
    public void saveToDat(WzManager wzManager) throws IOException {
        new VCoreDatLoader().saveDat(load(wzManager));
    }
}
