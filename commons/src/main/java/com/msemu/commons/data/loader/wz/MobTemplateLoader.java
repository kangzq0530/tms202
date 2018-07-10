package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.enums.Element;
import com.msemu.commons.data.enums.ElementalEffectiveness;
import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.MobTemplateDatLoader;
import com.msemu.commons.data.templates.MobTemplate;
import com.msemu.commons.utils.StringUtils;
import com.msemu.commons.wz.WzFile;
import com.msemu.commons.wz.WzImage;
import com.msemu.commons.wz.WzPropertyType;
import com.msemu.commons.wz.properties.WzSubProperty;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/4/25.
 */
public class MobTemplateLoader extends WzDataLoader<Map<Integer, MobTemplate>> {

    @Getter
    Map<Integer, MobTemplate> data = new HashMap<>();

    public MobTemplateLoader(WzManager wzManager) {
        super(wzManager);
    }

    @Override
    public void load() {
        WzFile mobWz = wzManager.getWz(WzManager.MOB);
        WzFile mob2Wz = wzManager.getWz(WzManager.MOB2);
        WzFile stringWz = wzManager.getWz(WzManager.STRING);
        List<WzImage> mobImages = new ArrayList<>();
        mobImages.addAll(mobWz.getWzDirectory().getImages());
        mobImages.addAll(mob2Wz.getWzDirectory().getImages());
        mobImages.forEach(image -> {
            MobTemplate mob = importMobImage(image);
            data.put(mob.getId(), mob);
        });
        WzImage mobStrImage = stringWz.getWzDirectory().getImage("Mob.img");
        importStrings(data, mobStrImage);
    }

    private void importStrings(Map<Integer, MobTemplate> data, WzImage mobStrImage) {
        data.forEach((id, mt) -> {
            if (mobStrImage.get(id.toString()) != null) {
                mt.setName("NoName");
                WzSubProperty mobProp = (WzSubProperty) mobStrImage.get(id.toString());
                mobProp.getProperties().forEach(p -> {
                    if (p.getName().equalsIgnoreCase("name")) {
                        mt.setName(p.getString());
                    }
                });
            }
        });
    }

    private MobTemplate importMobImage(WzImage image) {
        int mobId = Integer.parseInt(image.getName().replace(".img", ""));
        MobTemplate mt = new MobTemplate();
        mt.setId(mobId);
        WzSubProperty infoProp = (WzSubProperty) image.get("info");
        infoProp.getProperties().forEach(prop -> {
            if (prop.getName().equalsIgnoreCase("finalmaxHP")) {
                if (StringUtils.isNumber(prop.getString())) {
                    mt.setFinalmaxHP(prop.getLong());
                } else {
                    mt.setFinalmaxHP(0);
                }
            } else if (prop.getName().equalsIgnoreCase("maxHP")) {
                if (StringUtils.isNumber(prop.getString())) {
                    mt.setMaxHP(prop.getLong());
                } else {
                    mt.setMaxHP(50000000);
                }
            } else if (prop.getName().equalsIgnoreCase("maxMP")) {
                if (StringUtils.isNumber(prop.getString())) {
                    mt.setMaxMP(prop.getInt());
                } else {
                    mt.setMaxMP(50000000);
                }

            } else if (prop.getName().equalsIgnoreCase("exp")) {
                mt.setExp(prop.getLong());
            } else if (prop.getName().equalsIgnoreCase("level")) {
                mt.setLevel(prop.getShort());
            } else if (prop.getName().equalsIgnoreCase("charismaEXP")) {
                mt.setCharismaEXP(prop.getShort());
            } else if (prop.getName().equalsIgnoreCase("wp")) {
                mt.setWp(prop.getShort());
            } else if (prop.getName().equalsIgnoreCase("removeAfter")) {
                mt.setRemoveAfter(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("rareItemDropLevel")) {
                mt.setRareItemDropLevel((byte) prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("fixedDamage")) {
                mt.setFixedDamage(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("onlyNormalAttack")) {
                mt.setOnlyNormalAttack(prop.getInt() > 0);
            } else if (prop.getName().equalsIgnoreCase("boss")) {
                mt.setBoss(prop.getInt() > 0);
            } else if (prop.getName().equalsIgnoreCase("skeleton")) {
                mt.setSkeleton(prop.getInt() > 0);
            } else if (prop.getName().equalsIgnoreCase("explosiveReward")) {
                mt.setExplosiveReward(prop.getInt() > 0);
            } else if (prop.getName().equalsIgnoreCase("undead")) {
                mt.setUndead(prop.getInt() > 0);
            } else if (prop.getName().equalsIgnoreCase("escort")) {
                mt.setEscort(prop.getInt() > 0);
            } else if (prop.getName().equalsIgnoreCase("partyBonusMob")) {
                mt.setPartyBonusMob(prop.getInt() > 0);
            } else if (prop.getName().equalsIgnoreCase("partyBonusR")) {
                mt.setPartyBonusR(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("buff")) {
                if (prop.propType() == WzPropertyType.Int) {
                    mt.setBuff(prop.getInt());
                } else if (prop.propType() == WzPropertyType.String) {
                    mt.setBuff(Integer.parseInt(prop.getString()));
                }
            } else if (prop.getName().equalsIgnoreCase("changeableMob")) {
                mt.setChangeableMob(prop.getInt() > 0);
            } else if (prop.getName().equalsIgnoreCase("damagedByMob")) {
                mt.setDamagedByMob(prop.getInt() > 0);
            } else if (prop.getName().equalsIgnoreCase("noDoom")) {
                mt.setNoDoom(prop.getInt() > 0);
            } else if (prop.getName().equalsIgnoreCase("publicReward")) {
                mt.setPublicReward(prop.getInt() > 0);
            } else if (prop.getName().equalsIgnoreCase("getCP")) {
                mt.setGetCP(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("point")) {
                mt.setPoint(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("dropItemPeriod")) {
                mt.setDropItemPeriod(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("PADamage")) {
                mt.setPADamage(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("PDDamage")) {
                mt.setPDDamage(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("MADamage")) {
                mt.setMADamage(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("MDDamage")) {
                mt.setMDDamage(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("PDRate")) {
                mt.setPDRate(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("MDRate")) {
                mt.setMDRate(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("acc")) {
                mt.setAcc(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("eva")) {
                mt.setEva(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("summonType")) {
                mt.setSummonType(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("category")) {
                mt.setCategory(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("speed")) {
                mt.setSpeed(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("pushed")) {
                mt.setPushed(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("selfDestruction")) {
                WzSubProperty sdProp = (WzSubProperty) prop;
                sdProp.getProperties().forEach(p -> {
                    if (p.getName().equalsIgnoreCase("hp")) {
                        mt.setSelfDestructionHP(p.getInt());
                    } else if (p.getName().equalsIgnoreCase("removeAfter")) {
                        mt.setSelfDestructionRemoveAfter(p.getInt());
                    } else if (p.getName().equalsIgnoreCase("action")) {
                        mt.setSelfDestructionAction(p.getInt());
                    }
                });
            } else if (prop.getName().equalsIgnoreCase("hpTagColor")) {
                mt.setHpTagColor(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("hpTagBgcolor")) {
                mt.setHpTagBgcolor(prop.getInt());
            } else if (prop.getName().equalsIgnoreCase("ban")) {
                WzSubProperty banProp = (WzSubProperty) prop;
                banProp.getProperties().forEach(p -> {
                    if (p.getName().equalsIgnoreCase("banMsg")) {
                        mt.setBanMsg(p.getString());
                    } else if (p.getName().equalsIgnoreCase("banMap")) {
                        if (p.hasProperty("0")) {
                            WzSubProperty banMapProp = (WzSubProperty) ((WzSubProperty) p).get("0");
                            banMapProp.getProperties().forEach(pp -> {
                                if (pp.getName().equalsIgnoreCase("field")) {
                                    mt.setBanMapFieldId(pp.getInt());
                                } else if (pp.getName().equalsIgnoreCase("portal")) {
                                    mt.setBanMapPortalName(pp.getString());
                                }
                            });
                        }
                    } else if (p.getName().equalsIgnoreCase("action")) {
                        mt.setSelfDestructionAction(p.getInt());
                    }
                });
            } else if (prop.getName().equalsIgnoreCase("revive")) {
                WzSubProperty reviveProp = (WzSubProperty) prop;
                reviveProp.getProperties().forEach(p -> mt.addRevive(p.getInt()));
            } else if (prop.getName().equalsIgnoreCase("skill")) {
                WzSubProperty skillsProp = (WzSubProperty) prop;
                skillsProp.getProperties().stream().map(each -> (WzSubProperty) each).forEach(each -> {
                    final int[] data = new int[2];
                    each.getProperties().forEach(pp -> {
                        if (pp.getName().equalsIgnoreCase("level")) {
                            data[1] = pp.getInt();
                        } else if (pp.getName().equalsIgnoreCase("skill")) {
                            data[0] = pp.getInt();
                        }
                    });
                    mt.addSkill(data[0], data[1]);
                });
            } else if (prop.getName().equalsIgnoreCase("elemAttr")) {
                String elemAttr = prop.getString();
                for (int i = 0; i < elemAttr.length(); i += 2) {
                    Element e = Element.getFromChar(Character.toString(elemAttr.charAt(i)));
                    ElementalEffectiveness ef = ElementalEffectiveness.getByNumber(Integer.valueOf(String.valueOf(elemAttr.charAt(i + 1))));
                    mt.getBasicElemAttrs().put(e, ef);
                }

            } else if (prop.getName().equalsIgnoreCase("link")) {
                mt.setLink(prop.getInt());
            }
        });
        return mt;
    }

    @Override
    public void saveToDat() throws IOException {
        if(getData().isEmpty())
            load();
        new MobTemplateDatLoader().saveDat(getData());
    }
}
