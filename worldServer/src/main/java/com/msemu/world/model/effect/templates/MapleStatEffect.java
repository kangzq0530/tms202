package com.msemu.world.model.effect.templates;

import com.msemu.commons.data.provider.MapleDataTool;
import com.msemu.commons.data.provider.interfaces.MapleData;
import com.msemu.commons.utils.types.Pair;
import com.msemu.world.dataholders.constants.GameConstants;
import com.msemu.world.dataholders.constants.ItemConstants;
import com.msemu.world.dataholders.constants.SkillConstants;
import com.msemu.world.model.player.enums.MapleTraitType;
import com.msemu.world.model.skills.buff.enums.MapleBuffStat;
import com.msemu.world.model.skills.buff.enums.MapleDisease;
import com.msemu.world.model.skills.buff.enums.MapleStatInfo;
import com.msemu.world.model.skills.buff.enums.MonsterStatus;
import com.msemu.world.utils.StatEffectUtils;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/3/21.
 */
public class MapleStatEffect implements Serializable {

    private int sourceId;

    private int level;

    private boolean isSkill;

    private boolean overTime;

    private int consumeOnPickup;

    private int charColor;

    private Point lt, rb, lt2, rb2;

    public Map<MapleStatInfo, Integer> info = new EnumMap<>(MapleStatInfo.class);

    private Map<MapleTraitType, Integer> traits = new EnumMap<>(MapleTraitType.class);

    private List<MapleDisease> diseasesBuff = new ArrayList<>();

    private List<Integer> petsCanConsume = new ArrayList<>();

    private List<Integer> familiars = new ArrayList<>();

    private List<Integer> randomPickup = new ArrayList<>();

    private List<Pair<Integer, Integer>> mapConditions = new ArrayList<>();

    private Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);

    private Map<MonsterStatus, Integer> monsterStatus = new EnumMap<>(MonsterStatus.class);


    private int mobSkill;

    private int mobSkillLevel;

    private int fatigueChange;

    private int familiarTarget;


    public MapleStatEffect(int sourceId, int level, MapleData source, boolean isSkill) {
        this.sourceId = sourceId;
        this.level = level;
        this.isSkill = isSkill;
        this.load(source);
    }

    private static void addBuffStatPairToListIfNotZero(final Map<MapleBuffStat, Integer> list, final MapleBuffStat buffStat, final Integer value) {
        if (value != 0) {
            list.put(buffStat, value);
        }
    }


    public final void load(MapleData source) {
        for (final MapleStatInfo prop : MapleStatInfo.values()) {
            String propName = prop.isSpecial() ? prop.name().substring(0, prop.name().length() - 1) : prop.name();
            if (source.getChildByPath(propName) != null)
                this.info.put(prop, StatEffectUtils.parseEval(propName, source, prop.getDefault(), "x", level));
        }
        this.consumeOnPickup = StatEffectUtils.parseEval("consumeOnPickup", source, 0, "x", level);
        if (this.consumeOnPickup == 1) {
            if (StatEffectUtils.parseEval("party", source, 0, "x", level) > 0) {
                this.consumeOnPickup = 2;
            }
        }

        for (MapleTraitType t : MapleTraitType.values()) {
            int traitExp = StatEffectUtils.parseEval(t.name() + "EXP", source, 0, "x", level);
            if (traitExp != 0) {
                this.traits.put(t, traitExp);
            }
        }
        if (StatEffectUtils.parseEval("poison", source, 0, "x", level) > 0) {
            this.diseasesBuff.add(MapleDisease.中毒);
        }
        if (StatEffectUtils.parseEval("seal", source, 0, "x", level) > 0) {
            this.diseasesBuff.add(MapleDisease.封印);
        }
        if (StatEffectUtils.parseEval("darkness", source, 0, "x", level) > 0) {
            this.diseasesBuff.add(MapleDisease.黑暗);
        }
        if (StatEffectUtils.parseEval("weakness", source, 0, "x", level) > 0) {
            this.diseasesBuff.add(MapleDisease.虛弱);
        }
        if (StatEffectUtils.parseEval("curse", source, 0, "x", level) > 0) {
            this.diseasesBuff.add(MapleDisease.詛咒);
        }


        final MapleData ltd = source.getChildByPath("lt");
        final MapleData rbd = source.getChildByPath("rb");
        final MapleData lt2d = source.getChildByPath("lt");
        final MapleData rb2d = source.getChildByPath("rb");

        if (ltd != null) {
            this.lt = MapleDataTool.getPoint(ltd);
            this.rb = MapleDataTool.getPoint(rbd);
            if (lt2d != null) {
                this.lt2 = MapleDataTool.getPoint(lt2d);
                this.rb2 = MapleDataTool.getPoint(rb2d);
            }
        }

        final MapleData cond = source.getChildByPath("con");

        if (cond != null) {
            for (MapleData entry : cond) {
                this.mapConditions.add(new Pair<>(MapleDataTool.getInt("sMap", entry, 0), MapleDataTool.getInt("eMap", entry, 999999999)));
            }
        }

        if (!isSkill) {
            charColor = 0;
            String cColor = MapleDataTool.getString("charColor", source, null);
            if (cColor != null) {
                this.charColor |= Integer.parseInt("0x" + cColor);
            }

            if (ItemConstants.類型.寵物食品(sourceId)) {
                for (int i = 0; ; i++) {
                    final int value = StatEffectUtils.parseEval(String.valueOf(i), source, 0, "", level);
                    if (value > 0) {
                        this.petsCanConsume.add(value);
                    } else {
                        break;
                    }
                }
            }

            final MapleData randomPickupData = source.getChildByPath("randomPickup");
            if (randomPickupData != null) {
                for (MapleData entry : randomPickupData) {
                    this.randomPickup.add(MapleDataTool.getInt(entry));
                }
            }

            final MapleData mdd = source.getChildByPath("0");
            if (mdd != null && mdd.getChildren().size() > 0) {
                this.mobSkill = StatEffectUtils.parseEval("mobSkill", mdd, 0, "x", level);
                this.mobSkillLevel = StatEffectUtils.parseEval("level", mdd, 0, "x", level);
            } else {
                this.mobSkill = 0;
                this.mobSkillLevel = 0;
            }

            final MapleData familiarData = source.getChildByPath("familiar");
            if (familiarData != null) {
                this.fatigueChange = (StatEffectUtils.parseEval("incFatigue", familiarData, 0, null, level) - StatEffectUtils.parseEval("decFatigue", familiarData, 0, null, level));
                this.familiarTarget = StatEffectUtils.parseEval("target", familiarData, 0, null, level);
                final MapleData targetListData = familiarData.getChildByPath("targetList");
                if (targetListData != null) {
                    for (MapleData ltz : targetListData) {
                        this.familiars.add(MapleDataTool.getInt(ltz, 0));
                    }
                }
            } else {
                this.fatigueChange = 0;
            }
        } else {

            // skill

            final int priceUnit = getInfo(MapleStatInfo.priceUnit); // Guild skills
            if (priceUnit > 0) {
                final int price = getInfo(MapleStatInfo.price);
                final int extendPrice = getInfo(MapleStatInfo.extendPrice);
                putInfo(MapleStatInfo.price, price * priceUnit);
                putInfo(MapleStatInfo.extendPrice, extendPrice * priceUnit);
            }

            if (!this.isSkill && getInfo(MapleStatInfo.time) > -1) {
                this.overTime = true;
            } else {
                // skill time change unit from s to ms
                int time = getInfo(MapleStatInfo.time);
                if (time > 0) {
                    putInfo(MapleStatInfo.time, time * 1000);
                    this.overTime = time > 0;
                }

            }
            if (this.overTime && SkillConstants.getSummonMovementType(sourceId) == null && !SkillConstants.isEnergyCharge(sourceId) && !isSkill) {
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.PAD, getInfo(MapleStatInfo.pad));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.PDD, getInfo(MapleStatInfo.pdd));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.MAD, getInfo(MapleStatInfo.mad));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.ACC, getInfo(MapleStatInfo.acc));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.EVA, getInfo(MapleStatInfo.eva));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.Speed, sourceId == 32120001 || sourceId == 32120014 || sourceId == 32101003 ? getInfo(MapleStatInfo.x) : getInfo(MapleStatInfo.speed));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.Jump, getInfo(MapleStatInfo.jump));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.MaxMP, getInfo(MapleStatInfo.mhpR));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.MaxHP, getInfo(MapleStatInfo.mmpR));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.Booster, getInfo(MapleStatInfo.booster));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.Thaw, getInfo(MapleStatInfo.thaw));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.ExpBuffRate, getInfo(MapleStatInfo.expBuff)); // EXP
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.ItemUpByItem, GameConstants.getModifier(sourceId, getInfo(MapleStatInfo.itemupbyitem))); // defaults to 2x
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.MesoUpByItem, GameConstants.getModifier(sourceId, getInfo(MapleStatInfo.mesoupbyitem))); // defaults to 2x
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.Barrier, getInfo(MapleStatInfo.illusion));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.DojangBerserk, getInfo(MapleStatInfo.berserk));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.EMHP, getInfo(MapleStatInfo.emhp));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.EMMP, getInfo(MapleStatInfo.emmp));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.EPAD, getInfo(MapleStatInfo.epad));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.EMAD, getInfo(MapleStatInfo.emad));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.EPDD, getInfo(MapleStatInfo.epdd));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.Inflation, getInfo(MapleStatInfo.inflation));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.STR, getInfo(MapleStatInfo.str));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.DEX, getInfo(MapleStatInfo.dex));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.INT, getInfo(MapleStatInfo.int_));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.LUK, getInfo(MapleStatInfo.luk));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndiePAD, getInfo(MapleStatInfo.indiePad));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndieMAD, getInfo(MapleStatInfo.indieMad));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndieMHP, getInfo(MapleStatInfo.imhp));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndieMMP, getInfo(MapleStatInfo.immp)); //same one? lol
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndieMHPR, getInfo(MapleStatInfo.indieMhpR));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndieMMPR, getInfo(MapleStatInfo.indieMmpR));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndieMHP, getInfo(MapleStatInfo.indieMhp));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndieMMP, getInfo(MapleStatInfo.indieMmp));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.PVPDamage, getInfo(MapleStatInfo.incPVPDamage));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndieJump, getInfo(MapleStatInfo.indieJump));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndieSpeed, getInfo(MapleStatInfo.indieSpeed));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndieACC, getInfo(MapleStatInfo.indieAcc));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndieEVA, getInfo(MapleStatInfo.indieEva));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndieAllStat, getInfo(MapleStatInfo.indieAllStat));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.IndieBooster, getInfo(MapleStatInfo.indieBooster));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.PVPDamageSkill, getInfo(MapleStatInfo.PVPdamage));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.NotDamaged, getInfo(MapleStatInfo.immortal));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.Event2, getInfo(MapleStatInfo.preventslip));
                addBuffStatPairToListIfNotZero(this.statups, MapleBuffStat.Event2, charColor > 0 ? 1 : 0);
            }

        }


    }

    public Integer getInfo(MapleStatInfo prop) {
        if (info.containsKey(prop)) {
            return info.get(prop);
        }
        return prop.getDefault();
    }

    public void putInfo(MapleStatInfo prop, Integer value) {
        this.info.put(prop, value);
    }

    public void putStatup(MapleBuffStat stat, Integer value) {
        this.statups.put(stat, value);
    }

    public void putMonsterStatus(MonsterStatus stat, Integer value) {
        this.monsterStatus.put(stat, value);
    }


}
