package com.msemu.world.client.field.lifes;


import com.msemu.commons.data.templates.MobTemplate;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Tuple;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.field.LP_FieldEffect;
import com.msemu.core.network.packets.outpacket.mob.LP_MobEnterField;
import com.msemu.core.network.packets.outpacket.mob.LP_MobHpIndicator;
import com.msemu.core.network.packets.outpacket.mob.LP_MobLeaveField;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.ExpIncreaseInfo;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.effect.MobHPTagFieldEffect;
import com.msemu.world.client.field.lifes.skills.MobSkill;
import com.msemu.world.client.field.lifes.skills.MobTemporaryStat;
import com.msemu.world.constants.GameConstants;
import com.msemu.world.enums.DeathType;
import com.msemu.world.enums.FieldObjectType;
import com.msemu.world.enums.MobAppearType;
import lombok.Getter;
import lombok.Setter;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Created by Weber on 2018/4/11.
 */
@Getter
@Setter
public class Mob extends AbstractInternalAnimatedLife {
    @Getter
    private final MobTemplate template;
    @Getter
    private int originFh;
    @Getter
    @Setter
    private boolean sealedInsteadDead, patrolMob, eliteMob;
    @Getter
    @Setter
    private int option, effectItemID, patrolScopeX1, patrolScopeX2, detectX, senseX, phase, curZoneDataType;
    @Getter
    @Setter
    private int refImgMobID, lifeReleaseOwnerAID, afterAttack, currentAction = -1, scale = 100, targetUserIdFromServer;
    @Getter
    @Setter
    private long hp;
    @Getter
    @Setter
    private int mp;
    @Getter
    @Setter
    private byte calcDamageIndex = 1, moveAction, teamForMCarnival;
    @Getter
    @Setter
    private MobAppearType appearType = MobAppearType.Delay;
    @Getter
    @Setter
    private DeathType deathType = DeathType.ANIMATION_DEATH;
    @Getter
    @Setter
    private String lifeReleaseOwnerName = "", lifeReleaseMobName = "";
    @Getter
    @Setter
    private ShootingMoveStat shootingMoveStat;
    @Getter
    @Setter
    private ForcedMobStat forcedMobStat;
    @Getter
    private MobTemporaryStat temporaryStat;
    @Getter
    private Map<Character, Long> damageDone = new HashMap<>();
    @Getter
    private List<DropInfo> dropsInfo = new ArrayList<>();
    @Getter
    private List<MobSkill> skills = new ArrayList<>();
    @Getter
    @Setter
    private Field field;
    @Getter
    @Setter
    private IMobListener mobListener;
    private WeakReference<Character> controller = new WeakReference<>(null);
    @Getter
    @Setter
    private int controllerLevel;
    @Getter
    @Setter
    private EliteMobInfo eliteMobInfo = null;

    public Mob(int objectId, MobTemplate template) {
        super(template.getId());
        super.setObjectId(objectId);
        this.template = template;
        temporaryStat = new MobTemporaryStat(this);
        scale = 100;
        calcDamageIndex = 1;
        controllerLevel = 1;
        deathType = DeathType.getByValue(template.getSelfDestructionAction());
    }

    public Mob deepCopy() {
        Mob copy = new Mob(getObjectId(), getTemplate());
        // start life
        copy.getPosition().setX(getPosition().getX());
        copy.getPosition().setY(getPosition().getY());
        copy.setF(getF());
        copy.setFh(getFh());
        copy.setOriginFh(getOriginFh());
        // end life
        copy.setSealedInsteadDead(isSealedInsteadDead());
        copy.setPatrolMob(isPatrolMob());
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
        copy.setTargetUserIdFromServer(getTargetUserIdFromServer());
        copy.setHp(getHp());
        copy.setCalcDamageIndex(getCalcDamageIndex());
        copy.setMoveAction(getMoveAction());
        copy.setAppearType(getAppearType());
        copy.setTeamForMCarnival(getTeamForMCarnival());
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
        copy.setDropsInfo(getDropsInfo()); // doesn't get mutated, so should be fine
        getSkills().forEach(copy::addSkill);
        getQuests().forEach(copy::addQuest);
        return copy;
    }

    public boolean isBoss() {
        return getTemplate().isBoss();
    }

    public boolean isFirstAttack() {
        return getTemplate().isFirstAttack();
    }

    public String getName() {
        return getTemplate().getName();
    }

    public void damage(Long totalDamage) {
        long maxHP = getMaxHp();
        long oldHp = getHp();
        long newHp = oldHp - totalDamage;
        setHp(newHp);
        double hpPercentAfterDamage = ((double) newHp / maxHP);
        newHp = newHp > Integer.MAX_VALUE ? Integer.MAX_VALUE : newHp;
        if (newHp <= 0) {
            die();
            getField().broadcastPacket(new LP_FieldEffect(new MobHPTagFieldEffect(this)));
        } else if (isBoss()) {
            getField().broadcastPacket(new LP_FieldEffect(new MobHPTagFieldEffect(this)));
        } else {
            getField().broadcastPacket(new LP_MobHpIndicator(getObjectId(), (byte) (hpPercentAfterDamage * 100)));
        }
    }


    private void dropDrops() {
        getField().drop(getDropsInfo(), getField().getFootholdById(getFh()), getPosition(), getMostDamageCharacter().getId());
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
        long exp = getExp();
        long totalDamage = getDamageDone().values().stream().mapToLong(l -> l).sum();
        for (Character chr : getDamageDone().keySet()) {
            double damagePercent = getDamageDone().get(chr) / (double) totalDamage;
            long appliedExp = (long) (exp * damagePercent);
            ExpIncreaseInfo eii = new ExpIncreaseInfo();
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

    public boolean isEscort() {
        return getTemplate().isEscort();
    }

    public void addQuest(int questID) {
        getQuests().add(questID);
    }

    public void init() {
        setHp(getTemplate().getMaxHP());
        setMp(getTemplate().getMaxMP());
    }

    public int getHpTagColor() {
        return getTemplate().getHpTagColor();
    }

    public int getHpTagBgcolor() {
        return getTemplate().getHpTagBgcolor();
    }

    public long getExp() {
        return getForcedMobStat() != null ? getForcedMobStat().getExp() : getTemplate().getExp();
    }

    public long getMaxHp() {
        return getForcedMobStat() != null ? getForcedMobStat().getMaxHP() : getTemplate().getMaxHP();
    }

    public int getMaxMp() {
        return getForcedMobStat() != null ? getForcedMobStat().getMaxMP() : getTemplate().getMaxMP();
    }

    public int getSpeed() {
        return getForcedMobStat() != null ? getForcedMobStat().getSpeed() : getTemplate().getSpeed();
    }

    public int getLevel() {
        return getForcedMobStat() != null ? getForcedMobStat().getLevel() : getTemplate().getLevel();
    }

    public int getPad() {
        return getForcedMobStat() != null ? getForcedMobStat().getPad() : getTemplate().getPADamage();
    }

    public int getMad() {
        return getForcedMobStat() != null ? getForcedMobStat().getMad() : getTemplate().getMADamage();
    }

    public int getPdr() {
        return getForcedMobStat() != null ? getForcedMobStat().getPdr() : getTemplate().getPDRate();
    }

    public int getMdr() {
        return getForcedMobStat() != null ? getForcedMobStat().getMdr() : getTemplate().getMDRate();
    }

    public int getAcc() {
        return getForcedMobStat() != null ? getForcedMobStat().getAcc() : getTemplate().getAcc();
    }

    public int getEva() {
        return getForcedMobStat() != null ? getForcedMobStat().getEva() : getTemplate().getEva();
    }

    public int getPushed() {
        return getForcedMobStat() != null ? getForcedMobStat().getPushed() : getTemplate().getPushed();
    }

    public int getUserCount() {
        return getForcedMobStat() != null ? getForcedMobStat().getUserCount() : 0;
    }


    public boolean isAlive() {
        return getHp() > 0;
    }

    public Character getController() {
        return controller.get();
    }

    public void setController(Character character) {
        controller = new WeakReference<Character>(character);
    }

    @Override
    public FieldObjectType getFieldObjectType() {
        return FieldObjectType.MOB;
    }


    public void die() {
        getField().removeMob(this);
        distributeExp();
        dropDrops(); // xd
        for (Character chr : getDamageDone().keySet()) {
            chr.getQuestManager().handleMobKill(this);
        }
        getDamageDone().clear();
        if (getMobListener() != null) {
            getMobListener().die();
            setMobListener(null);
        }
        getField().broadcastPacket(new LP_MobLeaveField(this));
    }

    @Override
    public void enterScreen(GameClient client) {
        client.write(new LP_MobEnterField(this, false));
        if (getField() != null) {
            if (!isEscort()) {
                Position charPos = client.getCharacter().getPosition();
                if (charPos.distance(getPosition()) <= GameConstants.maxViewRangeSq()) {
                    getField().updateMobController(this);
                }
            }
        }
    }

    @Override
    public void outScreen(GameClient client) {
        if (!isEscort()) {
            client.write(new LP_MobLeaveField(this, DeathType.STAY));
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append("(");
        sb.append(getTemplate());
        sb.append(")[");
        sb.append(getObjectId());
        sb.append("]LV.");
        sb.append(getTemplate().getLevel());
        if (getForcedMobStat() != null) {
            sb.append("→");
            sb.append(getLevel());
        }
        sb.append("(");
        sb.append(getPosition().getX());
        sb.append(", ");
        sb.append(getPosition().getY());
        sb.append(")HP:");
        sb.append(getHp());
        sb.append("/");
        sb.append(getMaxHp());
        sb.append(",MP:");
        sb.append(getMp());
        sb.append("/");
        sb.append(getMaxMp());
        sb.append(",仇恨:");
        final Character chr = controller.get();
        sb.append(chr != null ? chr.getName() : "無");
        return sb.toString();
    }

    public int getEliteGrade() {
        return getEliteMobInfo() == null ? -1 : getEliteMobInfo().getGrade();
    }
}
