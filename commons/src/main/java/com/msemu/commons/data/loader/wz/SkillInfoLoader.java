package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.enums.Element;
import com.msemu.commons.data.enums.SkillStat;
import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.SkillInfoDatLoader;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.utils.StringUtils;
import com.msemu.commons.utils.types.Rect;
import com.msemu.commons.wz.WzDirectory;
import com.msemu.commons.wz.WzFile;
import com.msemu.commons.wz.WzImage;
import com.msemu.commons.wz.WzImageProperty;
import com.msemu.commons.wz.properties.WzStringProperty;
import com.msemu.commons.wz.properties.WzSubProperty;
import com.msemu.commons.wz.properties.WzVectorProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/4/23.
 */
public class SkillInfoLoader extends WzDataLoader<Map<Integer, SkillInfo>> {
    private static final Logger log = LoggerFactory.getLogger(SkillInfoLoader.class);

    @Override
    public Map<Integer, SkillInfo> load(WzManager wzManager) {
        Map<Integer, SkillInfo> data = new HashMap<>();
        WzFile skillWZ = wzManager.getWz(WzManager.SKILL);
        WzDirectory wzDir = skillWZ.getWzDirectory();
        wzDir.getImages().stream()
                .filter(img -> img.getName().matches("^[0-9]+.img$"))
                .map(img -> (WzSubProperty) img.getFromPath("skill"))
                .forEach(infoProp -> infoProp.getProperties().forEach(skillProp -> {
                    int skillId = skillProp.getInt();
                    SkillInfo skillInfo = importSkill((WzSubProperty) skillProp);
                    skillInfo.setSkillId(skillId);
                    data.put(skillId, skillInfo);
                }));
        WzFile stringWZ = wzManager.getWz(WzManager.STRING);
        WzImage stringImg = stringWZ.getWzDirectory().getImage("Skill.img");

        data.forEach((skillID, skillInfo) -> {
            String paddedId = StringUtils.getLeftPaddedStr(skillID.toString(), '0', 7);
            WzImageProperty nameProp = stringImg.getFromPath(paddedId + "/name");
            WzImageProperty descProp = stringImg.getFromPath(paddedId + "/desc");
            if (nameProp != null) {
                if (nameProp instanceof WzStringProperty)
                    skillInfo.setName(nameProp.getString());
            }
            if (descProp != null) {
                if (descProp instanceof WzStringProperty)
                    skillInfo.setDesc(descProp.getString());
            }
        });
        return data;
    }


    private SkillInfo importSkill(WzSubProperty skillProp) {
        SkillInfo skillInfo = new SkillInfo();
        skillInfo.setRootId(Integer.parseInt(skillProp.getParent().getParent().getName().replace(".img", "")));

        skillProp.getProperties().forEach(prop -> {
            String propName = prop.getName();
            if (propName.equalsIgnoreCase("skillType"))
                skillInfo.setSkillType(prop.getInt());
            else if (propName.equalsIgnoreCase("elemAttr"))
                skillInfo.setElemAttr(Element.getFromChar(prop.getString()));
            else if (propName.equalsIgnoreCase("invisible"))
                skillInfo.setInvisible(prop.getInt() > 0);
            else if (propName.equalsIgnoreCase("notRemoved"))
                skillInfo.setNotRemoved(prop.getInt() > 0);
            else if (propName.equalsIgnoreCase("timeLimited"))
                skillInfo.setTimeLimited(prop.getInt() > 0);
            else if (propName.equalsIgnoreCase("combatOrders"))
                skillInfo.setCombatOrders(prop.getInt() > 0);
            else if (propName.equalsIgnoreCase("fixLevel"))
                skillInfo.setFixLevel(prop.getInt());
            else if (propName.equalsIgnoreCase("masterLevel"))
                skillInfo.setMasterLevel(prop.getInt());
            else if (propName.equalsIgnoreCase("psd"))
                skillInfo.setPsd(prop.getInt() > 0);
            else if (propName.equalsIgnoreCase("psdSkill")) {
                WzSubProperty psdSkillProp = (WzSubProperty) prop;
                psdSkillProp.getProperties().forEach(p -> skillInfo.addPsdSkill(p.getInt()));
            } else if (propName.equalsIgnoreCase("eventTamingMob"))
                skillInfo.setEventTamingMob(prop.getInt());
            else if (propName.equalsIgnoreCase("vehicleID"))
                skillInfo.setVehicleID(prop.getInt());
            else if (propName.equalsIgnoreCase("hyper"))
                skillInfo.setHyper(prop.getInt());
            else if (propName.equalsIgnoreCase("hyperStat"))
                skillInfo.setHyperStat(prop.getInt());
            else if (propName.equalsIgnoreCase("reqLev"))
                skillInfo.setReqLev(prop.getInt());
            else if (propName.equalsIgnoreCase("vSkill"))
                skillInfo.setVSkill(prop.getInt() > 0);
            else if (propName.equalsIgnoreCase("petPassive"))
                skillInfo.setPetPassive(prop.getInt() > 0);
            else if (propName.equalsIgnoreCase("setItemReason"))
                skillInfo.setSetItemReason(prop.getInt());
            else if (propName.equalsIgnoreCase("setItemPartsCount"))
                skillInfo.setSetItemPartsCount(prop.getInt());
            else if (propName.equalsIgnoreCase("info")) {
                if (prop.hasProperty("pvp"))
                    skillInfo.setPvp(prop.getFromPath("pvp").getInt() > 0);
                if (prop.hasProperty("magicDamage"))
                    skillInfo.setMagicDamage(prop.getFromPath("magicDamage").getInt() > 0);
                if (prop.hasProperty("casterMove"))
                    skillInfo.setCasterMove(prop.getFromPath("casterMove").getInt() > 0);
                if (prop.hasProperty("pushTarget"))
                    skillInfo.setPushTarget(prop.getFromPath("pushTarget").getInt() > 0);
                if (prop.hasProperty("pullTarget"))
                    skillInfo.setPullTarget(prop.getFromPath("pullTarget").getInt() > 0);
            } else if (propName.equalsIgnoreCase("effect")) {
                // TODO 儲存skill delay
            } else if (propName.equalsIgnoreCase("common")) {
                WzSubProperty commonProp = (WzSubProperty) prop;
                skillInfo.setMaxLevel(1);
                HashMap<SkillStat, String> skillStatInfo = new HashMap<>();
                commonProp.getProperties().forEach(p -> {
                    String subPrpname = p.getName();
                    if (subPrpname.equalsIgnoreCase("maxLevel"))
                        skillInfo.setMaxLevel(p.getInt());
                    else if (subPrpname.matches("^lt[0_9]+")) {
                        String num = subPrpname.replace("lt", "");
                        WzVectorProperty lt = (WzVectorProperty) prop.getFromPath("lt" + num);
                        WzVectorProperty rb = (WzVectorProperty) prop.getFromPath("rb" + num);
                        Rect rect = new Rect(lt.getPoint(), rb.getPoint());
                        skillInfo.addRect(rect);
                    } else {
                        SkillStat skillStat = SkillStat.getSkillStatByString(subPrpname);
                        if (skillStat == null) {
                            if(log.isWarnEnabled()) {
                                log.warn("Unknown SkillStat: " + subPrpname);
                            }
                            return;
                        }
                        skillStatInfo.put(skillStat, p.getString());
                    }
                });
                for (int i = 1; i <= skillInfo.getMaxLevel(); i++) {
                    skillInfo.getSkillStatInfo().put(i, skillStatInfo);
                }
            } else if (propName.equalsIgnoreCase("level")) {
                ((WzSubProperty) prop).getProperties()
                        .stream()
                        .map(pp -> (WzSubProperty) pp)
                        .forEach(pp -> {
                            int slv = Integer.parseInt(pp.getName());
                            pp.getProperties().forEach(ppp -> {
                                String pppName = ppp.getName();
                                if (pppName.matches("^lt[0_9]+")) {
                                    String num = pppName.replace("lt", "");
                                    WzVectorProperty lt = (WzVectorProperty) prop.getFromPath("lt" + num);
                                    WzVectorProperty rb = (WzVectorProperty) prop.getFromPath("rb" + num);
                                    Rect rect = new Rect(lt.getPoint(), rb.getPoint());
                                    skillInfo.addRect(rect);
                                } else {
                                    SkillStat skillStat = SkillStat.getSkillStatByString(pppName);
                                    if (skillStat == null) {
                                        if(log.isWarnEnabled()) {
                                            log.warn("Unknown SkillStat: " + pppName);
                                        }
                                        return;
                                    }
                                    skillInfo.addSkillStatInfo(slv, skillStat, ppp.getString());
                                }
                            });
                        });
                skillInfo.setMaxLevel(skillInfo.getSkillStatInfo().size());
            }
        });
        return skillInfo;
    }


    @Override
    public void saveToDat(WzManager wzManager) throws IOException {
        new SkillInfoDatLoader().saveDat(load(wzManager));
    }
}
