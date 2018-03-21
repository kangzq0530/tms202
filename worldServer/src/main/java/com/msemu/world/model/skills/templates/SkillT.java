package com.msemu.world.model.skills.templates;

import com.msemu.commons.data.provider.MapleDataTool;
import com.msemu.commons.data.provider.interfaces.MapleData;
import com.msemu.world.model.effect.templates.MapleStatEffect;
import com.msemu.world.model.skills.enums.ElementAttribute;
import com.msemu.world.model.skills.buff.enums.MapleStatInfo;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Weber on 2018/3/21.
 */
public class SkillT {

    private int id;

    private int skillType;

    private ElementAttribute elementAttribute;

    private boolean invisible;

    private boolean notRemoved;

    private boolean timeLimited;

    private boolean combatOrders;

    private boolean petPassive;

    private boolean pvpDisabled;

    private boolean magicDamage;

    private boolean areaAttack;

    private boolean casterMove;

    private boolean pushTarget;

    private boolean pullTarget;

    private boolean chargeSkill;

    private boolean hyperStat;

    private boolean hasSummon;

    private int fixLevel;

    private int masterLevel;

    private int maxLevel;

    private int psd;

    private int animationTime;

    private int requiredSkill;

    private int requiredSkillLevel;

    private int requiredLevel;

    private int eventTamingMob;

    private int skillTamingMob;

    private int vehicleID;

    private int hyper;

    private int reqLev;

    private int vSkill;

    private int setItemReason;

    private int setItemPartsCount;

    private int trueMaxLevel;

    private Map<Integer, AbstractMap.Entry<MapleStatInfo, Integer>> psdSkills = new HashMap<>();

    private List<MapleStatEffect> effects = new ArrayList<>();

    private List<MapleStatEffect> pvpEffects = new ArrayList<>();


    public SkillT(MapleData source) {

        LoggerFactory.getLogger(SkillT.class).info(String.format("[SKILL] " + source.getName()));

        this.id = Integer.parseUnsignedInt(source.getName());
        this.skillType = MapleDataTool.getInt("skillType", source, -1);
        this.elementAttribute = ElementAttribute.fromString(MapleDataTool.getString("elemAttr", source, ""));
        this.invisible = MapleDataTool.getBoolean("invisible", source, false);
        this.notRemoved = MapleDataTool.getBoolean("notRemoved", source, false);
        this.timeLimited = MapleDataTool.getBoolean("timeLimited", source, false);
        this.combatOrders = MapleDataTool.getBoolean("combatOrders", source, false);
        this.areaAttack = MapleDataTool.getBoolean("areaAttack", source, false);
        this.fixLevel = MapleDataTool.getInt("fixLevel", source, 0);
        this.masterLevel = MapleDataTool.getInt("masterLevel", source, 0);
        this.psd = MapleDataTool.getInt("psd", source, 0);


        this.chargeSkill = source.getChildByPath("keydown") != null;


        final MapleData psdSkillData = source.getChildByPath("psdSkill");
        if (psdSkillData != null) {
            psdSkillData.getChildren().forEach(skill -> {
                Integer psdId = Integer.parseInt(skill.getName());
                AbstractMap.SimpleEntry<MapleStatInfo, Integer> entry = null;
                if (skill.getChildren().size() > 0) {
                    MapleStatInfo attr = MapleStatInfo.valueOf(skill.getChildren().get(0).getName());
                    Integer value = MapleDataTool.getInt(skill.getChildren().get(0), 0);
                    entry = new AbstractMap.SimpleEntry<>(attr, value);
                }
                psdSkills.put(psdId, entry);
            });
        }

        this.eventTamingMob = MapleDataTool.getInt("eventTamingMob", source, 0);
        this.skillTamingMob = MapleDataTool.getInt("skillTamingMob", source, 0);
        this.vehicleID = MapleDataTool.getInt("vehicleID", source, 0);
        this.hyper = MapleDataTool.getInt("hyper", source, 0);
        this.hyperStat = MapleDataTool.getBoolean("hyperStat", source, false);
        this.reqLev = MapleDataTool.getInt("reqLev", source, 0);
        this.vSkill = MapleDataTool.getInt("vSkill", source, 0);
        this.petPassive = MapleDataTool.getBoolean("petPassive", source, false);
        this.setItemReason = MapleDataTool.getInt("setItemReason", source, 0);
        this.setItemPartsCount = MapleDataTool.getInt("setItemPartsCount", source, 0);

        final MapleData info = source.getChildByPath("info");

        if (info != null) {
            this.pvpDisabled = !(MapleDataTool.getBoolean("pvp", info, true));
            this.magicDamage = MapleDataTool.getBoolean("magicDamage", info, false);
            this.casterMove = MapleDataTool.getBoolean("casterMove", info, false);
            this.pushTarget = MapleDataTool.getBoolean("pushTarget", info, false);
            this.pullTarget = MapleDataTool.getBoolean("pullTarget", info, false);
        }

        this.animationTime = 0;
        final MapleData effect = source.getChildByPath("effect");
        if (effect != null) {
            effect.forEach(effectEntry -> {
                this.animationTime += MapleDataTool.getInt("delay", effectEntry, 0);
            });
        }

        final MapleData req = source.getChildByPath("req");

        if (req != null) {
            this.requiredLevel = MapleDataTool.getInt("level", req, 0);
            req.forEach(reqSkill -> {
                if (reqSkill.getName().equals("level") || reqSkill.getName().equals("reqAmount") ||
                        reqSkill.getName().equals("reqTierPoint"))
                    return;
                this.requiredSkill = Integer.parseInt(reqSkill.getName());
                this.requiredSkillLevel = MapleDataTool.getInt(reqSkill, 1);
            });
        }

        final MapleData commonData = source.getChildByPath("common");

        if(commonData != null) {
            this.maxLevel = MapleDataTool.getInt("maxLevel", commonData, 1);
            this.trueMaxLevel = maxLevel + (combatOrders ? 2 : 0);
            for(int i = 1 ; i <= trueMaxLevel; i++) {
                this.effects.add(new MapleStatEffect(this.getId(), i, commonData, true));
            }
        } else {
            final MapleData levelData = source.getChildByPath("level");
            levelData.getChildren().forEach(levelEntry->{
                this.effects.add(new MapleStatEffect(this.getId(), Integer.parseInt(levelEntry.getName()), levelEntry, true));
            });
            this.maxLevel = this.effects.size();
            this.trueMaxLevel= this.effects.size();
        }


        final MapleData pvpCommonData = source.getChildByPath("PVPCommon");

        if(pvpCommonData != null) {
            for(int i = 1 ; i <= trueMaxLevel; i++) {
                pvpEffects.add(new MapleStatEffect(this.getId(), i, pvpCommonData, true));
            }
        }

        this.hasSummon = source.getChildByPath("summon") != null;


        int infoType = MapleDataTool.getInt("info/type", source, -1);

        //LoggerFactory.getLogger(SkillT.class).info(String.format("[%s] type: %d", id, infoType));


    }


    public int getId() {
        return id;
    }

    public int getSkillType() {
        return skillType;
    }

    public ElementAttribute getElementAttribute() {
        return elementAttribute;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public boolean isNotRemoved() {
        return notRemoved;
    }

    public boolean isTimeLimited() {
        return timeLimited;
    }

    public boolean isCombatOrders() {
        return combatOrders;
    }

    public boolean isPetPassive() {
        return petPassive;
    }

    public boolean isPvpDisabled() {
        return pvpDisabled;
    }

    public boolean isMagicDamage() {
        return magicDamage;
    }

    public boolean isAreaAttack() {
        return areaAttack;
    }

    public boolean isCasterMove() {
        return casterMove;
    }

    public boolean isPushTarget() {
        return pushTarget;
    }

    public boolean isPullTarget() {
        return pullTarget;
    }

    public boolean isChargeSkill() {
        return chargeSkill;
    }

    public boolean isHyperStat() {
        return hyperStat;
    }

    public int getFixLevel() {
        return fixLevel;
    }

    public int getMasterLevel() {
        return masterLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getTrueMaxLevel() {
        return trueMaxLevel;
    }

    public int getPsd() {
        return psd;
    }

    public int getAnimationTime() {
        return animationTime;
    }

    public int getRequiredSkill() {
        return requiredSkill;
    }

    public int getRequiredSkillLevel() {
        return requiredSkillLevel;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public int getEventTamingMob() {
        return eventTamingMob;
    }

    public int getSkillTamingMob() {
        return skillTamingMob;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public int getHyper() {
        return hyper;
    }

    public int getReqLev() {
        return reqLev;
    }

    public int getvSkill() {
        return vSkill;
    }

    public int getSetItemReason() {
        return setItemReason;
    }

    public int getSetItemPartsCount() {
        return setItemPartsCount;
    }

    public Map<Integer, AbstractMap.Entry<MapleStatInfo, Integer>> getPsdSkills() {
        return psdSkills;
    }

    public List<MapleStatEffect> getEffects() {
        return effects;
    }

    public List<MapleStatEffect> getPvpEffects() {
        return pvpEffects;
    }
}
