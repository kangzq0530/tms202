package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.enums.InvType;
import com.msemu.commons.data.enums.ItemScrollStat;
import com.msemu.commons.data.enums.ItemSpecStat;
import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.ItemTemplateDatLoader;
import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.utils.StringUtils;
import com.msemu.commons.utils.types.Tuple;
import com.msemu.commons.wz.IPropertyContainer;
import com.msemu.commons.wz.WzDirectory;
import com.msemu.commons.wz.WzFile;
import com.msemu.commons.wz.WzImage;
import com.msemu.commons.wz.properties.WzIntProperty;
import com.msemu.commons.wz.properties.WzStringProperty;
import com.msemu.commons.wz.properties.WzSubProperty;
import lombok.Getter;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by Weber on 2018/4/21.
 */
public class ItemTemplateLoader extends WzDataLoader<Map<Integer, ItemTemplate>> {


    @Getter
    Map<Integer, ItemTemplate> data = new HashMap<>();

    public ItemTemplateLoader(WzManager wzManager) {
        super(wzManager);
    }


    private ItemTemplate loadProp(WzSubProperty itemProp) {
        WzSubProperty info = (WzSubProperty) itemProp.getFromPath("info");
        WzSubProperty specPorp = (WzSubProperty) itemProp.getFromPath("spec");
        ItemTemplate item = new ItemTemplate();
        item.setItemId(itemProp.getInt());
        List<String> tmp = new ArrayList<>();

        if (specPorp != null) {
            specPorp.getProperties().forEach(p -> {
                if (p.getName().equalsIgnoreCase("script")) {
                    item.getItemSpec().setScript(p.getString());
                } else if (p.getName().equalsIgnoreCase("npc")) {
                    item.getItemSpec().setNpc(p.getInt());
                } else if (p.getName().equalsIgnoreCase("con")) {
                    int sMap = p.getFromPath("sMap") != null ? p.getFromPath("sMap").getInt() : 0;
                    int eMap = p.getFromPath("eMap") != null ? p.getFromPath("eMap").getInt() : 0;
                    item.getItemSpec().getAvailableMapRanges().add(new Tuple<>(sMap, eMap));
                } else if (p.getName().equalsIgnoreCase("slotPerLine")) {
                    item.getItemSpec().setSlotPerLine(p.getInt());
                } else if (p.getName().equalsIgnoreCase("slotCount")) {
                    item.getItemSpec().setSlotCount(p.getInt());
                } else if (p.getName().equalsIgnoreCase("cosmetic")) {
                    item.getItemSpec().setCosmetic(p.getInt());
                } else if (p.getName().equalsIgnoreCase("incTameness")) {
                    item.getItemSpec().setIncTameness(p.getInt());
                } else if (p.getName().equalsIgnoreCase("incRepleteness")) {
                    item.getItemSpec().setIncRepleteness(p.getInt());
                } else if (p.getName().equalsIgnoreCase("BFSkill")) {
                    item.getItemSpec().setBFSkill(p.getInt());
                } else if (p.getName().equalsIgnoreCase("mobHp")) {
                    item.getItemSpec().setMobHp(p.getInt());
                } else if (p.getName().equalsIgnoreCase("mobID")) {
                    item.getItemSpec().setMobID(p.getInt());
                } else if (p.getName().equalsIgnoreCase("type")) {
                    item.getItemSpec().setType(p.getInt());
                } else if (p.getName().equalsIgnoreCase("incFatigue")) {
                    item.getItemSpec().setIncFatigue(p.getInt());
                } else if (p.getName().equalsIgnoreCase("startTime")) {
                    item.getItemSpec().setStartTime(p.getInt());
                } else if (p.getName().equalsIgnoreCase("bs")) {
                    item.getItemSpec().setBs(p.getInt());
                } else if (p.getName().equalsIgnoreCase("bsUp")) {
                    item.getItemSpec().setBsUp(p.getInt());
                } else if (p.getName().equalsIgnoreCase("useLevel")) {
                    item.getItemSpec().setUseLevel(p.getInt());
                } else if (p.getName().equalsIgnoreCase("skillID")) {
                    item.getItemSpec().setSkillID(p.getInt());
                } else if (p.getName().equalsIgnoreCase("recipe")) {
                    item.getItemSpec().setRecipe(p.getInt());
                } else if (p.getName().equalsIgnoreCase("reqSkillLevel")) {
                    item.getItemSpec().setReqSkillLevel(p.getInt());
                } else if (p.getName().equalsIgnoreCase("recipeUseCount")) {
                    item.getItemSpec().setRecipeUseCount(p.getInt());
                } else if (p.getName().equalsIgnoreCase("effectedOnAlly")) {
                    item.getItemSpec().setEffectedOnAlly(p.getInt() > 0);
                } else if (p.getName().equalsIgnoreCase("randomPickupConsume")) {
                    item.getItemSpec().setRandomPickupConsume(p.getInt() > 0);
                } else if (p.getName().equalsIgnoreCase("charColor")) {
                    item.getItemSpec().setCharColor(p.getString());
                } else if (p.getName().equalsIgnoreCase("randomPickup")) {
                    ((WzSubProperty) p).getProperties().forEach(pp -> {
                        item.getItemSpec().getRandomPickup().add(pp.getInt());
                    });
                } else if (p.getName().equalsIgnoreCase("reqSkill")) {
                    item.getItemSpec().setReqSkill(p.getInt());
                } else if (p.getName().equalsIgnoreCase("reqSkillProficiency")) {
                    item.getItemSpec().setReqSkillProficiency(p.getInt());
                } else if (p.getName().equalsIgnoreCase("recipeValidDay")) {
                    item.getItemSpec().setRecipeValidDay(p.getInt());
                } else if (p.getName().equalsIgnoreCase("interval")) {
                    item.getItemSpec().setInterval(p.getInt());
                } else if (p.getName().equalsIgnoreCase("familiarPassiveSkillTarget")) {
                    item.getItemSpec().setFamiliarPassiveSkillTarget(p.getInt());
                } else if (p.getName().equalsIgnoreCase("onlyPickup")) {
                    item.getItemSpec().setOnlyPickup(p.getInt() > 0);
                } else if (p.getName().equalsIgnoreCase("runOnPickup")) {
                    item.getItemSpec().setRunOnPickup(p.getInt() > 0);
                } else if (p.getName().equalsIgnoreCase("consumeOnPickup")) {
                    item.getItemSpec().setConsumeOnPickup(p.getInt() > 0);
                } else if (p.getName().equalsIgnoreCase("repeatEffect")) {
                    item.getItemSpec().setRepeatEffect(p.getInt() > 0);
                } else if (p.getName().equalsIgnoreCase("ignoreMixHair_jp")) {
                    item.getItemSpec().setIgnoreMixHair_jp(p.getInt() > 0);
                } else if (p.getName().equalsIgnoreCase("indieQrPointTerm")) {
                    ((WzSubProperty) p).getProperties().forEach(pp -> {
                        item.getItemSpec().getIndieQrPointTermStats().add(new Tuple<>(Integer.parseInt(pp.getName()), pp.getInt()));
                    });
                } else {
                    ItemSpecStat specStat = ItemSpecStat.getSpecStatByName(p.getName());
                    if (specStat != null) {
                        item.getItemSpec().putSpecStat(specStat, p.getInt());
                    } else if (StringUtils.isNumber(p.getName())) {
                        if (p instanceof WzIntProperty) {
                            item.getItemSpec().getPetsCanFeed().add(p.getInt());
                        } else {
                            int x = 1;
                        }
                    } else {
                        if(tmp.contains(p.getName()))
                            return;
                        tmp.add(p.getName());
                        LoggerFactory.getLogger(ItemTemplateLoader.class).info(String.format("ItemSpec: %s (%d) not loaded", p.getName(), item.getItemId()));
                    }
                }

            });
        }


        if (info != null)

        {
            info.getProperties().forEach(p -> {

                String propName = p.getName();
                if (propName.equalsIgnoreCase("time")) {
                    if (p instanceof WzIntProperty) {
                        item.setTime(p.getInt());
                    } else {
                        // TODO subDirProperty 加倍券時間格式
                    }
                } else if (propName.equalsIgnoreCase("cash")) {
                    item.setCash(p.getInt() > 0);
                } else if (propName.equalsIgnoreCase("stateChangeItem")) {
                    item.setStateChangeItem(p.getInt());
                } else if (propName.equalsIgnoreCase("cash")) {
                    item.setCash(p.getInt() > 0);
                } else if (propName.equalsIgnoreCase("stateChangeItem")) {
                    item.setStateChangeItem(p.getInt());
                } else if (propName.equalsIgnoreCase("price")) {
                    item.setPrice(p.getInt());
                } else if (propName.equalsIgnoreCase("tradeBlock")) {
                    item.setTradeBlock(p.getInt() > 0);
                } else if (propName.equalsIgnoreCase("notSale")) {
                    item.setNotSale(p.getInt() > 0);
                } else if (propName.equalsIgnoreCase("slotMax")) {
                    item.setSlotMax(p.getInt());
                } else if (propName.equalsIgnoreCase("notConsume")) {
                    item.setNotConsume(p.getInt() > 0);
                } else if (propName.equalsIgnoreCase("path")) {
                    item.setPath(p.getString());
                } else if (propName.equalsIgnoreCase("rate")) {
                    item.setRate(p.getInt());
                } else if (propName.equalsIgnoreCase("reqSkillLevel")) {
                    item.setReqSkillLevel(p.getInt());
                } else if (propName.equalsIgnoreCase("masterLevel")) {
                    item.setMasterLevel(p.getInt());
                } else if (propName.equalsIgnoreCase("success")) {
                    item.putScrollStat(ItemScrollStat.success, p.getInt());
                } else if (propName.equalsIgnoreCase("skill")) {
                    if (p instanceof WzSubProperty) {
                        WzSubProperty skill = (WzSubProperty) p;
                        skill.getProperties().forEach(skillProp -> item.getSkills().add(skillProp.getInt()));
                    } else if (p instanceof WzIntProperty) {
                        item.getSkills().add(p.getInt());
                    }
                } else if (propName.equalsIgnoreCase("noFlip")) {
                    item.setNoFlip(p.getString());
                } else if (propName.equalsIgnoreCase("reqLevel")) {
                    item.setReqLevel(p.getInt());
                } else if (propName.equalsIgnoreCase("karma")) {
                    item.setKarma(p.getInt());
                } else if (propName.equalsIgnoreCase("flatRate")) {
                    item.setFlatRate(p.getInt());
                } else if (propName.equalsIgnoreCase("limitMin")) {
                    item.setLimitMin(p.getInt());
                } else if (propName.equalsIgnoreCase("limitLess")) {
                    item.setLimitLess(p.getInt());
                } else if (propName.equalsIgnoreCase("accountSharable")) {
                    item.setAccountSharable(p.getInt() > 0);
                } else if (propName.equalsIgnoreCase("expireOnLogout")) {
                    item.setExpireOnLogout(p.getInt() > 0);
                } else if (propName.equalsIgnoreCase("timeLimited")) {
                    item.setTimeLimited(p.getInt() > 0);
                } else if (propName.equalsIgnoreCase("map")) {
                    item.setMap(p.getInt());
                } else if (propName.equalsIgnoreCase("sharedStatCostGrade")) {
                    item.setSharedStatCostGrade(p.getInt());
                } else if (propName.equalsIgnoreCase("levelVariation")) {
                    item.setLevelVariation(p.getInt());
                } else if (propName.equalsIgnoreCase("maxDays")) {
                    item.setMaxDays(p.getInt());
                } else if (propName.equalsIgnoreCase("addTime")) {
                    item.setAddTime(p.getInt());
                } else if (propName.equalsIgnoreCase("path4Top")) {
                    item.setPath4Top(p.getString());
                } else if (propName.equalsIgnoreCase("maxLevel")) {
                    item.setMaxLevel(p.getInt());
                } else if (propName.equalsIgnoreCase("pvpChannelLimited")) {
                    item.setPvpChannelLimited(p.getInt() > 0);
                } else if (propName.equalsIgnoreCase("minLevel")) {
                    item.setMinLevel(p.getInt());
                } else if (propName.equalsIgnoreCase("recoveryRate")) {
                    item.setRecoveryRate(p.getInt());
                } else if (propName.equalsIgnoreCase("soldInform")) {
                    item.setSoldInform(p.getInt() > 0);
                } else if (propName.equalsIgnoreCase("purchaseShop")) {
                    item.setPurchaseShop(p.getInt() > 0);
                } else if (propName.equalsIgnoreCase("recoveryHP")) {
                    item.setRecoveryHP(p.getInt());
                } else if (propName.equalsIgnoreCase("recoveryMP")) {
                    item.setRecoveryMP(p.getInt());
                } else if (propName.equalsIgnoreCase("tamingMob")) {
                    item.setTamingMob(p.getInt());
                } else if (propName.equalsIgnoreCase("dama")) {
                    item.setDama(p.getInt());
                } else if (propName.equalsIgnoreCase("sitEmotion")) {
                    item.setSitEmotion(p.getInt());
                } else if (propName.equalsIgnoreCase("meso")) {
                    item.setMeso(p.getInt());
                } else if (propName.equalsIgnoreCase("mesomin")) {
                    item.setMesomin(p.getInt());
                } else if (propName.equalsIgnoreCase("mesomax")) {
                    item.setMesomax(p.getInt());
                } else if (propName.equalsIgnoreCase("mesostdev")) {
                    item.setMesostdev(p.getInt());
                } else if (propName.equalsIgnoreCase("floatType")) {
                    item.setFloatType(p.getInt());
                } else if (propName.equalsIgnoreCase("type")) {
                    item.setType(p.getInt());
                } else if (propName.equalsIgnoreCase("direction")) {
                    item.setDirection(p.getInt());
                } else if (propName.equalsIgnoreCase("npc")) {
                    item.setNpcID(p.getInt());
                } else if (propName.equalsIgnoreCase("minusLevel")) {
                    item.setMinusLevel(p.getInt());
                } else if (propName.equalsIgnoreCase("skillEffectID")) {
                    item.setSkillEffectID(p.getInt());
                } else if (propName.equalsIgnoreCase("dressUpgrade")) {
                    item.setDressUpgrade(p.getInt());
                } else if (propName.equalsIgnoreCase("quest")) {
                    item.setQuest(p.getInt() > 0);
                } else if (propName.equalsIgnoreCase("questId")) {
                    item.addQuest(p.getInt());
                } else if (propName.equalsIgnoreCase("reqQuestOnProgress")) {
                    item.setReqQuestOnProgress(p.getInt());
                } else if (propName.equalsIgnoreCase("monsterBook")) {
                    item.setMonsterBook(p.getInt() > 0);
                } else if (propName.equalsIgnoreCase("mob")) {
                    item.setMobID(p.getInt());
                } else if (propName.equalsIgnoreCase("charmEXP") || propName.equalsIgnoreCase("charismaEXP")) {
                    item.setCharmEXP(p.getInt());
                } else if (propName.equalsIgnoreCase("senseEXP")) {
                    item.setSenseEXP(p.getInt());
                } else if (propName.equalsIgnoreCase("linkedID")) {
                    item.setLinkedID(p.getInt());
                } else if (propName.equalsIgnoreCase("noNegative")) {
                    item.putScrollStat(ItemScrollStat.noNegative, p.getInt());
                } else if (propName.equalsIgnoreCase("success")) {
                    item.putScrollStat(ItemScrollStat.success, p.getInt());
                } else if (propName.equalsIgnoreCase("incSTR")) {
                    item.putScrollStat(ItemScrollStat.incSTR, p.getInt());
                } else if (propName.equalsIgnoreCase("incDEX")) {
                    item.putScrollStat(ItemScrollStat.incDEX, p.getInt());
                } else if (propName.equalsIgnoreCase("incINT")) {
                    item.putScrollStat(ItemScrollStat.incINT, p.getInt());
                } else if (propName.equalsIgnoreCase("incLUK")) {
                    item.putScrollStat(ItemScrollStat.incLUK, p.getInt());
                } else if (propName.equalsIgnoreCase("incPAD")) {
                    item.putScrollStat(ItemScrollStat.incPAD, p.getInt());
                } else if (propName.equalsIgnoreCase("incMAD")) {
                    item.putScrollStat(ItemScrollStat.incMAD, p.getInt());
                } else if (propName.equalsIgnoreCase("incPDD")) {
                    item.putScrollStat(ItemScrollStat.incPDD, p.getInt());
                } else if (propName.equalsIgnoreCase("incMDD")) {
                    item.putScrollStat(ItemScrollStat.incMDD, p.getInt());
                } else if (propName.equalsIgnoreCase("incEVA")) {
                    item.putScrollStat(ItemScrollStat.incEVA, p.getInt());
                } else if (propName.equalsIgnoreCase("incACC")) {
                    item.putScrollStat(ItemScrollStat.incACC, p.getInt());
                } else if (propName.equalsIgnoreCase("incPERIOD")) {
                    item.putScrollStat(ItemScrollStat.incPERIOD, p.getInt());
                } else if (propName.equalsIgnoreCase("incMHP")) {
                    item.putScrollStat(ItemScrollStat.incMHP, p.getInt());
                } else if (propName.equalsIgnoreCase("incMaxHP")) {
                    item.putScrollStat(ItemScrollStat.incMHP, p.getInt());
                } else if (propName.equalsIgnoreCase("incMMP")) {
                    item.putScrollStat(ItemScrollStat.incMMP, p.getInt());
                } else if (propName.equalsIgnoreCase("incMaxMP")) {
                    item.putScrollStat(ItemScrollStat.incMMP, p.getInt());
                } else if (propName.equalsIgnoreCase("incSpeed")) {
                    item.putScrollStat(ItemScrollStat.incSpeed, p.getInt());
                } else if (propName.equalsIgnoreCase("incJump")) {
                    item.putScrollStat(ItemScrollStat.incJump, p.getInt());
                } else if (propName.equalsIgnoreCase("incReqLevel")) {
                    item.putScrollStat(ItemScrollStat.incReqLevel, p.getInt());
                } else if (propName.equalsIgnoreCase("randOption")) {
                    item.putScrollStat(ItemScrollStat.randOption, p.getInt());
                } else if (propName.equalsIgnoreCase("randStat")) {
                    item.putScrollStat(ItemScrollStat.randStat, p.getInt());
                } else if (propName.equalsIgnoreCase("tuc")) {
                    item.putScrollStat(ItemScrollStat.tuc, p.getInt());
                } else if (propName.equalsIgnoreCase("incIUC")) {
                    item.putScrollStat(ItemScrollStat.incIUC, p.getInt());
                } else if (propName.equalsIgnoreCase("speed")) {
                    item.putScrollStat(ItemScrollStat.speed, p.getInt());
                } else if (propName.equalsIgnoreCase("forceUpgrade")) {
                    item.putScrollStat(ItemScrollStat.forceUpgrade, p.getInt());
                } else if (propName.equalsIgnoreCase("cursed")) {
                    item.putScrollStat(ItemScrollStat.cursed, p.getInt());
                } else if (propName.equalsIgnoreCase("maxSuperiorEqp")) {
                    item.putScrollStat(ItemScrollStat.maxSuperiorEqp, p.getInt());
                } else if (propName.equalsIgnoreCase("reqRUC")) {
                    item.putScrollStat(ItemScrollStat.reqRUC, p.getInt());
                }

            });


        }


        return item;
    }

    private List<ItemTemplate> loadImage(WzImage subCates) {
        if (subCates.getName().equals("0433.img")) {
            int x = 1;
        }
        List<ItemTemplate> ret = new ArrayList<>();
        subCates.getProperties().stream().map(prop -> (WzSubProperty) prop).forEach(props -> {
            ret.add(loadProp(props));
        });
        return ret;
    }

    @Override
    public void load() {
        WzFile itemWz = wzManager.getWz(WzManager.ITEM);
        WzDirectory wzDir = itemWz.getWzDirectory();

        //TODO 寵物?
        String[] cateDirs = new String[]{"Cash", "Consume", "Etc", "Install", "Special"};

        Arrays.stream(cateDirs).forEach(cate -> {
            WzDirectory dir = wzDir.getDir(cate);
            dir.getImages().forEach(img -> {
                loadImage(img).forEach(item -> {
                    item.setInvType(InvType.getInvTypeByString(cate));
                    data.put(item.getItemId(), item);
                });
            });
        });

        WzFile stringFile = getWzManager().getWz(WzManager.STRING);
        WzImage etcImg = stringFile.getWzDirectory().getImage("Etc.img");
        importString((WzSubProperty) etcImg.getFromPath("Etc"));
        WzImage consumeImg = stringFile.getWzDirectory().getImage("Consume.img");
        importString(consumeImg);
        WzImage insImg = stringFile.getWzDirectory().getImage("Ins.img");
        importString(insImg);
        WzImage cashImg = stringFile.getWzDirectory().getImage("Cash.img");
        importString(cashImg);
    }

    private void importString(IPropertyContainer sub) {
        sub.getProperties().stream()
                .map(prop -> (WzSubProperty) prop)
                .forEach(itemProp -> {
                    final int itemId = Integer.parseInt(itemProp.getName());
                    if (data.containsKey(itemId)) {
                        if(itemProp.getFromPath("name") != null) {
                            WzStringProperty stringProperty =
                                    (WzStringProperty) itemProp.getFromPath("name");
                            data.get(itemId).setName(stringProperty.getString());
                        }
                        if(itemProp.getFromPath("desc") != null) {
                            WzStringProperty stringProperty =
                                    (WzStringProperty) itemProp.getFromPath("desc");
                            data.get(itemId).setDesc(stringProperty.getString());
                        }
                    }
                });
    }

    @Override
    public void saveToDat() throws IOException {
        if(getData().isEmpty()) {
            load();
        }
        new ItemTemplateDatLoader().saveDat(getData());
    }
}
