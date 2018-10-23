/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/4/23.
 */
public class SkillInfoLoader extends WzDataLoader<Map<Integer, SkillInfo>> {

    private static final Logger log = LoggerFactory.getLogger(SkillInfoLoader.class);

    @Getter
    Map<Integer, SkillInfo> data = new HashMap<>();

    public SkillInfoLoader(WzManager wzManager) {
        super(wzManager);
    }

    @Override
    public void load() {

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
            else if (propName.equalsIgnoreCase("finalAttack")) {
                if (prop instanceof WzSubProperty) {
                    WzSubProperty subProp = (WzSubProperty) prop;
                    subProp.getProperties().forEach(pp -> {
                        skillInfo.getFinalAttackSkills().add(Integer.parseInt(pp.getName()));
                    });
                }
            } else if (propName.equalsIgnoreCase("petPassive"))
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
                if (prop.hasProperty("finalAttack"))
                    skillInfo.setFinalAttack(prop.getFromPath("finalAttack").getInt() > 0);
            } else if (propName.equalsIgnoreCase("effect")) {
                // TODO 儲存skill delay
            } else if (propName.equalsIgnoreCase("common")) {
                WzSubProperty commonProp = (WzSubProperty) prop;
                skillInfo.addSkillStatByLevel(1, SkillStat.maxLevel, "1");
                commonProp.getProperties().forEach(p -> {
                    String subPropName = p.getName();
                   if (subPropName.matches("^lt[0-9]?")) {
                        String num = subPropName.replace("lt", "");
                        WzVectorProperty lt = (WzVectorProperty) prop.getFromPath("lt" + num);
                        WzVectorProperty rb = (WzVectorProperty) prop.getFromPath("rb" + num);
                        Rect rect = new Rect(lt.getPoint(), rb.getPoint());
                        if (num.equals("")) {
                            skillInfo.setRect1(rect);
                        } else if (num.equals("2")) {
                            skillInfo.setRect2(rect);
                        } else if (num.equals("3")) {
                            skillInfo.setRect3(rect);
                        } else if (num.equals("4")) {
                            skillInfo.setRect4(rect);
                        }
                    } else if (!subPropName.matches("^(lt|rb)[0-9]?")) {
                        SkillStat skillStat = SkillStat.getSkillStatByString(subPropName);
                        if (skillStat == null) {
                            if (log.isWarnEnabled()) {
                                log.warn("Unknown SkillStat: " + subPropName);
                            }
                            return;
                        }
                        skillInfo.addSkillStat(skillStat, p.getString());
                    }
                });

            } else if (propName.equalsIgnoreCase("psdWT")) {
                ((WzSubProperty) prop).getProperties().stream()
                        .map(p -> (WzSubProperty) p).forEach(wtProp -> {

                    Map<SkillStat, Integer> stats = new EnumMap<>(SkillStat.class);

                    wtProp.getProperties().forEach(pp -> {
                        SkillStat skillStat = SkillStat.getSkillStatByString(pp.getName());
                        if (skillStat == null) {
                            if (log.isWarnEnabled()) {
                                log.warn("Unknown psdWT SkillStat: " + pp.getName());
                            }
                            return;
                        }
                        stats.put(skillStat, Integer.parseInt(pp.getString()));
                    });
                    skillInfo.getPsdWT().put(Integer.parseInt(wtProp.getName()), stats);

                });
            } else if (propName.equalsIgnoreCase("level")) {
                ((WzSubProperty) prop).getProperties()
                        .stream()
                        .map(pp -> (WzSubProperty) pp)
                        .forEach(pp -> {
                            int slv = Integer.parseInt(pp.getName());
                            pp.getProperties().forEach(ppp -> {
                                String pppName = ppp.getName();
                                if (pppName.matches("^lt[0-9]?")) {
                                    String num = pppName.replace("lt", "");
                                    WzVectorProperty lt = (WzVectorProperty) pp.getFromPath("lt" + num);
                                    WzVectorProperty rb = (WzVectorProperty) pp.getFromPath("rb" + num);
                                    Rect rect = new Rect(lt.getPoint(), rb.getPoint());
                                    if (num.equals("")) {
                                        skillInfo.addRectByLevel(slv, rect);
                                    } else if (num.equals("2")) {
                                        skillInfo.addRect2ByLevel(slv, rect);
                                    } else if (num.equals("3")) {
                                        skillInfo.addRect3ByLevel(slv, rect);
                                    } else if (num.equals("4")) {
                                        skillInfo.addRect4ByLevel(slv, rect);
                                    }
                                } else if (!pppName.matches(("^(lt|rb)[0-9]?"))) {
                                    SkillStat skillStat = SkillStat.getSkillStatByString(pppName);
                                    if (skillStat == null) {
                                        if (log.isWarnEnabled()) {
                                            log.warn("Unknown SkillStat: " + pppName);
                                        }
                                        return;
                                    }
                                    skillInfo.addSkillStatByLevel(slv, skillStat, ppp.getString());
                                }
                            });
                        });
            }
        });
        return skillInfo;
    }

    @Override
    public void saveToDat() throws IOException {
        if (getData().isEmpty())
            load();
        new SkillInfoDatLoader().saveDat(getData());
    }
}
