package com.msemu.world.client.life;


import com.msemu.commons.data.templates.MobTemplate;
import com.msemu.commons.data.templates.field.Foothold;
import com.msemu.commons.thread.EventManager;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Tuple;
import com.msemu.core.network.packets.out.field.LP_FieldEffect;
import com.msemu.core.network.packets.out.mob.LP_MobHpIndicator;
import com.msemu.core.network.packets.out.mob.LP_MobLeaveField;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.ExpIncreaseInfo;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.effect.MobHPTagFieldEffect;
import com.msemu.world.client.life.skills.MobSkill;
import com.msemu.world.client.life.skills.MobTemporaryStat;
import com.msemu.world.enums.DeathType;
import com.msemu.world.enums.MobAppearType;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Created by Weber on 2018/4/11.
 */
@Getter
@Setter
public class Mob extends Life {

    private boolean sealedInsteadDead, patrolMob, isLeft;
    private int option, effectItemID, patrolScopeX1, patrolScopeX2, detectX, senseX, phase, curZoneDataType;
    private int refImgMobID, lifeReleaseOwnerAID, afterAttack, currentAction = -1, scale = 100, eliteGrade, eliteType, targetUserIdFromServer;
    private long hp, maxHp;
    private long mp, maxMp;
    private byte calcDamageIndex = 1, moveAction, teamForMCarnival;
    @Getter
    @Setter
    private MobAppearType appearType = MobAppearType.Normal;
    private Position prevPos;
    private Foothold curFoodhold;
    private Foothold homeFoothold;
    private String lifeReleaseOwnerName = "", lifeReleaseMobName = "";
    private ShootingMoveStat shootingMoveStat;
    private ForcedMobStat forcedMobStat;
    private MobTemporaryStat temporaryStat;
    private Map<Character, Long> damageDone = new HashMap<>();
    private List<DropInfo> dropsInfo = new ArrayList<>();
    private List<MobSkill> skills = new ArrayList<>();
    private Set<Integer> quests = new HashSet<>();
    private final MobTemplate template;

    public Mob(int objectId, MobTemplate template) {
        super(objectId);
        this.template = template;
        temporaryStat = new MobTemporaryStat(this);
        scale = 100;
        calcDamageIndex = 1;
        templateId = template.getId();
    }

    public Mob deepCopy() {
        Mob copy = new Mob(getObjectId(), getTemplate());
        // start life
        copy.setLifeType(getLifeType());
        copy.setTemplateId(getTemplateId());
        copy.setX(getX());
        copy.setY(getY());
        copy.setMobTime(getMobTime());
        copy.setF(getF());
        copy.setHide(isHide());
        copy.setFh(getFh());
        copy.setCy(getCy());
        copy.setRx0(getRx0());
        copy.setRx1(getRx1());
        copy.setLimitedName(getLimitedName());
        copy.setUseDay(isUseDay());
        copy.setUseNight(isUseNight());
        copy.setHold(isHold());
        copy.setNoFoothold(isNoFoothold());
        copy.setDummy(isDummy());
        copy.setSpine(isSpine());
        copy.setMobTimeOnDie(isMobTimeOnDie());
        copy.setRegenStart(getRegenStart());
        copy.setMobAliveReq(getMobAliveReq());
        // end life
        copy.setSealedInsteadDead(isSealedInsteadDead());
        copy.setPatrolMob(isPatrolMob());
        copy.setLeft(isLeft());
        copy.setOption(getOption());
        copy.setEffectItemID(getEffectItemID());
        copy.setPatrolScopeX1(getPatrolScopeX1());
        copy.setPatrolScopeX2(getPatrolScopeX2());
        copy.setDetectX(getDetectX());
        copy.setSenseX(getSenseX());
        copy.setPhase(getPhase());
        copy.setCurZoneDataType(getCurZoneDataType());
        copy.setRefImgMobID(getRefImgMobID());
        copy.setLifeReleaseOwnerAID(getLifeReleaseOwnerAID());
        copy.setAfterAttack(getAfterAttack());
        copy.setCurrentAction(getCurrentAction());
        copy.setScale(getScale());
        copy.setEliteGrade(getEliteGrade());
        copy.setEliteType(getEliteType());
        copy.setTargetUserIdFromServer(getTargetUserIdFromServer());
        copy.setHp(getHp());
        copy.setMaxHp(getMaxHp());
        copy.setCalcDamageIndex(getCalcDamageIndex());
        copy.setMoveAction(getMoveAction());
        copy.setAppearType(getAppearType());
        copy.setTeamForMCarnival(getTeamForMCarnival());
        if (getPrevPos() != null) {
            copy.setPrevPos(getPrevPos().deepCopy());
        }
        if (getCurFoodhold() != null) {
            copy.setCurFoodhold(getCurFoodhold().deepCopy());
        }
        if (getHomeFoothold() != null) {
            copy.setHomeFoothold(getHomeFoothold().deepCopy());
        }
        copy.setLifeReleaseOwnerName(getLifeReleaseOwnerName());
        copy.setLifeReleaseMobName(getLifeReleaseMobName());
        copy.setShootingMoveStat(null);
        if (getForcedMobStat() != null) {
            copy.setForcedMobStat(getForcedMobStat().deepCopy());
        }
        if (getTemporaryStat() != null) {
            copy.setTemporaryStat(getTemporaryStat().deepCopy());
        }
        copy.setMp(getMp());
        copy.setMaxMp(getMaxMp());
        copy.setDropsInfo(getDropsInfo()); // doesn't get mutated, so should be fine
        getSkills().forEach(copy::addSkill);
        getQuests().forEach(copy::addQuest);
        return copy;
    }

    public boolean isBoss() {
        return getTemplate().isBoss();
    }

    public ForcedMobStat getForcedMobStat() {
        return forcedMobStat;
    }

    public void setForcedMobStat(ForcedMobStat forcedMobStat) {
        this.forcedMobStat = forcedMobStat;
    }

    public boolean isPatrolMob() {
        return patrolMob;
    }

    public void setPatrolMob(boolean patrolMob) {
        this.patrolMob = patrolMob;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getEffectItemID() {
        return effectItemID;
    }

    public void setEffectItemID(int effectItemID) {
        this.effectItemID = effectItemID;
    }

    public int getPatrolScopeX1() {
        return patrolScopeX1;
    }

    public void setPatrolScopeX1(int patrolScopeX1) {
        this.patrolScopeX1 = patrolScopeX1;
    }

    public int getPatrolScopeX2() {
        return patrolScopeX2;
    }

    public void setPatrolScopeX2(int patrolScopeX2) {
        this.patrolScopeX2 = patrolScopeX2;
    }

    public int getDetectX() {
        return detectX;
    }

    public void setDetectX(int detectX) {
        this.detectX = detectX;
    }

    public int getSenseX() {
        return senseX;
    }

    public void setSenseX(int senseX) {
        this.senseX = senseX;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public int getCurZoneDataType() {
        return curZoneDataType;
    }

    public void setCurZoneDataType(int curZoneDataType) {
        this.curZoneDataType = curZoneDataType;
    }

    public int getRefImgMobID() {
        return refImgMobID;
    }

    public void setRefImgMobID(int refImgMobID) {
        this.refImgMobID = refImgMobID;
    }

    public int getLifeReleaseOwnerAID() {
        return lifeReleaseOwnerAID;
    }

    public void setLifeReleaseOwnerAID(int lifeReleaseOwnerAID) {
        this.lifeReleaseOwnerAID = lifeReleaseOwnerAID;
    }

    public int getAfterAttack() {
        return afterAttack;
    }

    public void setAfterAttack(int afterAttack) {
        this.afterAttack = afterAttack;
    }

    public int getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(int currentAction) {
        this.currentAction = currentAction;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getEliteGrade() {
        return eliteGrade;
    }

    public void setEliteGrade(int eliteGrade) {
        this.eliteGrade = eliteGrade;
    }

    public int getEliteType() {
        return eliteType;
    }

    public void setEliteType(int eliteType) {
        this.eliteType = eliteType;
    }

    public int getTargetUserIdFromServer() {
        return targetUserIdFromServer;
    }

    public void setTargetUserIdFromServer(int targetUserIdFromServer) {
        this.targetUserIdFromServer = targetUserIdFromServer;
    }

    public long getHp() {
        return hp;
    }

    public int getHpComparedToMaxHP() {
        if (getMaxHp() <= Integer.MAX_VALUE) {
            return (int) getHp();
        } else {
            return (int) (getHp() * (((double) Integer.MAX_VALUE) / getMaxHp()));
        }
    }

    public void setHp(long hp) {
        this.hp = hp;
    }

    public byte getCalcDamageIndex() {
        return calcDamageIndex;
    }

    public void setCalcDamageIndex(byte calcDamageIndex) {
        this.calcDamageIndex = calcDamageIndex;
    }

    public byte getMoveAction() {
        return moveAction;
    }

    public void setMoveAction(byte moveAction) {
        this.moveAction = moveAction;
    }

    public byte getTeamForMCarnival() {
        return teamForMCarnival;
    }

    public void setTeamForMCarnival(byte teamForMCarnival) {
        this.teamForMCarnival = teamForMCarnival;
    }

    public Position getPrevPos() {
        return prevPos;
    }

    public void setPrevPos(Position prevPos) {
        this.prevPos = prevPos;
    }

    public Foothold getCurFoodhold() {
        return curFoodhold;
    }

    public void setCurFoodhold(Foothold curFoodhold) {
        this.curFoodhold = curFoodhold;
    }

    public String getLifeReleaseOwnerName() {
        return lifeReleaseOwnerName;
    }

    public void setLifeReleaseOwnerName(String lifeReleaseOwnerName) {
        this.lifeReleaseOwnerName = lifeReleaseOwnerName;
    }

    public String getLifeReleaseMobName() {
        return lifeReleaseMobName;
    }

    public void setLifeReleaseMobName(String lifeReleaseMobName) {
        this.lifeReleaseMobName = lifeReleaseMobName;
    }

    public ShootingMoveStat getShootingMoveStat() {
        return shootingMoveStat;
    }

    public void setShootingMoveStat(ShootingMoveStat shootingMoveStat) {
        this.shootingMoveStat = shootingMoveStat;
    }

    public Foothold getHomeFoothold() {
        return homeFoothold;
    }

    public void setHomeFoothold(Foothold homeFoothold) {
        this.homeFoothold = homeFoothold;
    }

    public long getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(long maxHp) {
        this.maxHp = maxHp;
    }

    public long getMp() {
        return mp;
    }

    public void setMp(long mp) {
        this.mp = mp;
    }

    public long getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(long maxMp) {
        this.maxMp = maxMp;
    }

    public void setTemporaryStat(MobTemporaryStat temporaryStat) {
        this.temporaryStat = temporaryStat;
    }

    public MobTemporaryStat getTemporaryStat() {
        return temporaryStat;
    }

    public void damage(Long totalDamage) {
        long maxHP = getMaxHp();
        long oldHp = getHp();
        long newHp = oldHp - totalDamage;
        setHp(newHp);
        double percDamage = ((double) newHp / maxHP);
        newHp = newHp > Integer.MAX_VALUE ? Integer.MAX_VALUE : newHp;
        if (newHp <= 0) {
            die();
            getField().broadcastPacket(new LP_FieldEffect(new MobHPTagFieldEffect(this)));
        } else if (isBoss()) {
            getField().broadcastPacket(new LP_FieldEffect(new MobHPTagFieldEffect(this)));
        } else {
            getField().broadcastPacket(new LP_MobHpIndicator(getObjectId(), (byte) (percDamage * 100)));
        }
    }

    private void die() {
        Field field = getField();
        getField().broadcastPacket(new LP_MobLeaveField(getObjectId(), DeathType.ANIMATION_DEATH.getValue()));
        if (!isNotRespawnable()) { // double negative
            EventManager.getInstance().addEvent(() -> field.respawn(this), (long) (10000 * (1 / field.getMobRate())));
        } else {
            getField().removeLife(getObjectId());
        }
        field.putLifeController(this, null);
        distributeExp();
        dropDrops(); // xd
        setPosition(getHomePosition());
        for (Character chr : getDamageDone().keySet()) {
            chr.getQuestManager().handleMobKill(this);
        }
        getDamageDone().clear();
    }

    private void dropDrops() {
        getField().drop(getDropsInfo(), getField().getFootholdById(getFh()), getPosition(), getMostDamageCharacter().getId());
    }

    public Map<Character, Long> getDamageDone() {
        return damageDone;
    }

    public void addDamage(Character chr, long damage) {
        long cur = 0;
        if (getDamageDone().containsKey(chr)) {
            cur = getDamageDone().get(chr);
        }
        if (damage <= getHp()) {
            cur += damage;
        } else {
            cur += getHp();
        }
        getDamageDone().put(chr, cur);
    }

    public void distributeExp() {
        long exp = getForcedMobStat() != null ? getForcedMobStat().getExp() : getExp();
        long totalDamage = getDamageDone().values().stream().mapToLong(l -> l).sum();
        for (Character chr : getDamageDone().keySet()) {
            double damagePerc = getDamageDone().get(chr) / (double) totalDamage;
            long appliedExp = (long) (exp * damagePerc);
            ExpIncreaseInfo eii = chr.getExpIncreaseInfo();
            eii.setLastHit(true);
            eii.setIncEXP(appliedExp > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) appliedExp);
            chr.addExp(appliedExp, eii);
        }
    }

    public Character getMostDamageCharacter() {
        Tuple<Character, Long> max = new Tuple<>(null, (long) -1);
        for (Map.Entry<Character, Long> entry : getDamageDone().entrySet()) {
            Character chr = entry.getKey();
            long damage = entry.getValue();
            if (damage > max.getRight()) {
                max.setLeft(chr);
                max.setRight(damage);
            }
        }
        return max.getLeft();
    }

    public List<MobSkill> getSkills() {
        return skills;
    }

    public void addSkill(MobSkill skill) {
        getSkills().add(skill);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Mob) {
            Mob mob = (Mob) obj;
            return mob.getTemplateId() == getTemplateId() && mob.getObjectId() == getObjectId() && mob.getField().equals(getField());
        }
        return false;
    }

    public Set<Integer> getQuests() {
        return getTemplate().getLinkedQuests();
    }

    public void setQuests(Set<Integer> quests) {
        this.quests = quests;
    }

    public void addQuest(int questID) {
        getQuests().add(questID);
    }

    public void init() {
        setMaxHp(getTemplate().getMaxHP());
        setHp(getTemplate().getMaxHP());
        setMaxMp(getTemplate().getMaxMP());
        setMp(getTemplate().getMaxMP());
    }

    public int getHpTagColor() {
        return getTemplate().getHpTagColor();
    }

    public int getHpTagBgcolor() {
        return getTemplate().getHpTagBgcolor();
    }

    public long getExp() {
        return getTemplate().getExp();
    }
}
