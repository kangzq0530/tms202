package com.msemu.world.client.life.skills;

import com.msemu.commons.data.enums.MobStat;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.thread.EventManager;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.out.mob.MobStatReset;
import com.msemu.core.network.packets.out.mob.MobStatSet;
import com.msemu.world.client.character.skills.Option;
import com.msemu.world.client.character.skills.Skill;
import com.msemu.world.client.life.Mob;
import com.msemu.world.data.SkillData;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static com.msemu.commons.data.enums.MobStat.*;
import static com.msemu.commons.data.enums.MobStat.BurnedInfo;
import static com.msemu.commons.data.enums.SkillStat.*;

/**
 * Created by Weber on 2018/4/12.
 */
public class MobTemporaryStat {
    @Getter
    private List<BurnedInfo> burnedInfo = new ArrayList<>();
    @Getter
    private Map<Integer, ScheduledFuture> burnCancelSchedules = new HashMap<>();
    @Getter
    private Map<Integer, ScheduledFuture> burnSchedules = new HashMap<>();
    @Getter
    @Setter
    private String linkTeam;
    @Getter
    private Comparator<MobStat> mobStatComparator = (k1, k2) -> {
        int res = 0;
        if (k1.getPosition() < k2.getPosition()) {
            res = -1;
        } else if (k1.getPosition() > k2.getPosition()) {
            res = 1;
        } else {
            if (k1.getValue() < k2.getValue()) {
                res = -1;
            } else if (k1.getValue() > k2.getValue()) {
                res = 1;
            }
        }
        return res;
    };
    @Getter
    private TreeMap<MobStat, Option> currentStatVals = new TreeMap<>(mobStatComparator);
    @Getter
    private TreeMap<MobStat, Option> newStatVals = new TreeMap<>(mobStatComparator);
    @Getter
    private TreeMap<MobStat, Option> removedStatVals = new TreeMap<>(mobStatComparator);
    @Getter
    private Map<MobStat, ScheduledFuture> schedules = new HashMap<>();
    @Getter
    @Setter
    private Mob mob;

    public MobTemporaryStat(Mob mob) {
        this.mob = mob;
    }

    public MobTemporaryStat deepCopy() {
        MobTemporaryStat copy = new MobTemporaryStat(getMob());
        for (BurnedInfo bi : getBurnedInfo()) {
            copy.getBurnedInfo().add(bi.deepCopy());
        }
        copy.setLinkTeam(getLinkTeam());
        copy.mobStatComparator = getMobStatComparator();
        for (MobStat ms : getCurrentStatVals().keySet()) {
            copy.addStatOptions(ms, getCurrentStatVals().get(ms).deepCopy());
        }
        return copy;
    }

    public Option getNewOptionsByMobStat(MobStat mobStat) {
        return getNewStatVals().getOrDefault(mobStat, null);
    }

    public Option getCurrentOptionsByMobStat(MobStat mobStat) {
        return getCurrentStatVals().getOrDefault(mobStat, null);
    }

    public Option getRemovedOptionsByMobStat(MobStat mobStat) {
        return getRemovedStatVals().getOrDefault(mobStat, null);
    }

    public void encode(OutPacket<GameClient> outPacket) {
        // DecodeBuffer(12) + MobStat::DecodeTemporary
        int[] mask = getNewMask();
        for (int i = 0; i < mask.length; i++) {
            outPacket.encodeInt(mask[i]);
        }

        for (Map.Entry<MobStat, Option> entry : getNewStatVals().entrySet()) {
            MobStat mobStat = entry.getKey();
            Option option = entry.getValue();
            switch (mobStat) {
                case PAD:
                case PDR:
                case MAD:
                case MDR:
                case ACC:
                case EVA:
                case Speed:
                case Stun:
                case Freeze:
                case Poison:
                case Seal:
                case Darkness:
                case PowerUp:
                case MagicUp:
                case PGuardUp:
                case MGuardUp:
                case PImmune:
                case MImmune:
                case Web:
                case HardSkin:
                case Ambush:
                case Venom:
                case Blind:
                case SealSkill:
                case Dazzle:
                case PCounter:
                case MCounter:
                case RiseByToss:
                case BodyPressure:
                case Weakness:
                case Showdown:
                case MagicCrash:
                case DamagedElemAttr:
                case Dark:
                case Mystery:
                case AddDamParty:
                case HitCriDamR:
                case Fatality:
                case Lifting:
                case DeadlyCharge:
                case Smite:
                case AddDamSkill:
                case Incizing:
                case DodgeBodyAttack:
                case DebuffHealing:
                case AddDamSkill2:
                case BodyAttack:
                case TempMoveAbility:
                case FixDamRBuff:
                case ElementDarkness:
                case AreaInstallByHit:
                case BMageDebuff:
                case JaguarProvoke:
                case JaguarBleeding:
                case DarkLightning:
                case PinkbeanFlowerPot:
                case BattlePvPHelenaMark:
                case PsychicLock:
                case PsychicLockCoolTime:
                case PsychicGroundMark:
                case PowerImmune:
                case PsychicForce:
                case MultiPMDR:
                case ElementResetBySummon:
                case BahamutLightElemAddDam:
                case BossPropPlus:
                case MultiDamSkill:
                case RWLiftPress:
                case RWChoppingHammer:
                case TimeBomb:
                case Treasure:
                case AddEffect:
                case Invincible:
                case Explosion:
                case HangOver:
                    outPacket.encodeInt(getNewOptionsByMobStat(mobStat).nOption);
                    outPacket.encodeInt(getNewOptionsByMobStat(mobStat).rOption);
                    outPacket.encodeShort(getNewOptionsByMobStat(mobStat).tOption / 500);
            }
        }
        if (hasNewMobStat(PDR)) {
            outPacket.encodeInt(getNewOptionsByMobStat(PDR).cOption);
        }
        if (hasNewMobStat(MDR)) {
            outPacket.encodeInt(getNewOptionsByMobStat(MDR).cOption);
        }
        if (hasNewMobStat(PCounter)) {
            outPacket.encodeInt(getNewOptionsByMobStat(PCounter).wOption);
        }
        if (hasNewMobStat(MCounter)) {
            outPacket.encodeInt(getNewOptionsByMobStat(MCounter).wOption);
        }
        if (hasNewMobStat(PCounter)) {
            outPacket.encodeInt(getNewOptionsByMobStat(PCounter).mOption); // nCounterProb
            outPacket.encodeInt(getNewOptionsByMobStat(PCounter).bOption); // bCounterDelay
            outPacket.encodeInt(getNewOptionsByMobStat(PCounter).nReason); // nAggroRank
        } else if (hasNewMobStat(MCounter)) {
            outPacket.encodeInt(getNewOptionsByMobStat(MCounter).mOption); // nCounterProb
            outPacket.encodeInt(getNewOptionsByMobStat(MCounter).bOption); // bCounterDelay
            outPacket.encodeInt(getNewOptionsByMobStat(MCounter).nReason); // nAggroRank
        }
        if (hasNewMobStat(Fatality)) {
            outPacket.encodeInt(getNewOptionsByMobStat(Fatality).wOption);
            outPacket.encodeInt(getNewOptionsByMobStat(Fatality).uOption);
            outPacket.encodeInt(getNewOptionsByMobStat(Fatality).pOption);
            outPacket.encodeInt(getNewOptionsByMobStat(Fatality).yOption);
            outPacket.encodeInt(getNewOptionsByMobStat(Fatality).mOption);
        }
        if (hasNewMobStat(Explosion)) {
            outPacket.encodeInt(getNewOptionsByMobStat(Explosion).wOption);
        }
        if (hasNewMobStat(ExtraBuffStat)) {
            List<Option> values = getNewOptionsByMobStat(ExtraBuffStat).extraOpts;
            outPacket.encodeByte(values.size() > 0);
            if (values.size() > 0) {
                outPacket.encodeInt(getNewOptionsByMobStat(ExtraBuffStat).extraOpts.get(0).nOption); // nPAD
                outPacket.encodeInt(getNewOptionsByMobStat(ExtraBuffStat).extraOpts.get(0).mOption); // nMAD
                outPacket.encodeInt(getNewOptionsByMobStat(ExtraBuffStat).extraOpts.get(0).xOption); // nPDR
                outPacket.encodeInt(getNewOptionsByMobStat(ExtraBuffStat).extraOpts.get(0).yOption); // nMDR
            }
        }
        if (hasNewMobStat(DeadlyCharge)) {
            outPacket.encodeInt(getNewOptionsByMobStat(DeadlyCharge).pOption);
            outPacket.encodeInt(getNewOptionsByMobStat(DeadlyCharge).pOption);
        }
        if (hasNewMobStat(Incizing)) {
            outPacket.encodeInt(getNewOptionsByMobStat(Incizing).wOption);
            outPacket.encodeInt(getNewOptionsByMobStat(Incizing).uOption);
            outPacket.encodeInt(getNewOptionsByMobStat(Incizing).pOption);
        }
        if (hasNewMobStat(Speed)) {
            outPacket.encodeByte(getNewOptionsByMobStat(Speed).mOption);
        }
        if (hasNewMobStat(BMageDebuff)) {
            outPacket.encodeInt(getNewOptionsByMobStat(BMageDebuff).cOption);
        }
        if (hasNewMobStat(DarkLightning)) {
            outPacket.encodeInt(getNewOptionsByMobStat(DarkLightning).cOption);
        }
        if (hasNewMobStat(BattlePvPHelenaMark)) {
            outPacket.encodeInt(getNewOptionsByMobStat(BattlePvPHelenaMark).cOption);
        }
        if (hasNewMobStat(MultiPMDR)) {
            outPacket.encodeInt(getNewOptionsByMobStat(MultiPMDR).cOption);
        }
        if (hasNewMobStat(Freeze)) {
            outPacket.encodeInt(getNewOptionsByMobStat(Freeze).cOption);
        }
        if (hasNewMobStat(BurnedInfo)) {
            outPacket.encodeByte(getBurnedInfo().size());
            for (BurnedInfo bi : getBurnedInfo()) {
                bi.encode(outPacket);
            }
        }
        if (hasNewMobStat(InvincibleBalog)) {
            outPacket.encodeByte(getNewOptionsByMobStat(InvincibleBalog).nOption);
            outPacket.encodeByte(getNewOptionsByMobStat(InvincibleBalog).bOption);
        }
        if (hasNewMobStat(ExchangeAttack)) {
            outPacket.encodeByte(getNewOptionsByMobStat(ExchangeAttack).bOption);
        }
        if (hasNewMobStat(AddDamParty)) {
            outPacket.encodeInt(getNewOptionsByMobStat(AddDamParty).wOption);
            outPacket.encodeInt(getNewOptionsByMobStat(AddDamParty).pOption);
            outPacket.encodeInt(getNewOptionsByMobStat(AddDamParty).cOption);
        }
        if (hasNewMobStat(LinkTeam)) {
            outPacket.encodeString(getLinkTeam());
        }
        if (hasNewMobStat(SoulExplosion)) {
            outPacket.encodeInt(getNewOptionsByMobStat(SoulExplosion).nOption);
            outPacket.encodeInt(getNewOptionsByMobStat(SoulExplosion).rOption);
            outPacket.encodeInt(getNewOptionsByMobStat(SoulExplosion).wOption);
        }
        if (hasNewMobStat(SeperateSoulP)) {
            outPacket.encodeInt(getNewOptionsByMobStat(SeperateSoulP).nOption);
            outPacket.encodeInt(getNewOptionsByMobStat(SeperateSoulP).rOption);
            outPacket.encodeShort(getNewOptionsByMobStat(SeperateSoulP).tOption / 500);
            outPacket.encodeInt(getNewOptionsByMobStat(SeperateSoulP).wOption);
            outPacket.encodeInt(getNewOptionsByMobStat(SeperateSoulP).uOption);
        }
        if (hasNewMobStat(SeperateSoulC)) {
            outPacket.encodeInt(getNewOptionsByMobStat(SeperateSoulC).nOption);
            outPacket.encodeInt(getNewOptionsByMobStat(SeperateSoulC).rOption);
            outPacket.encodeShort(getNewOptionsByMobStat(SeperateSoulC).tOption / 500);
            outPacket.encodeInt(getNewOptionsByMobStat(SeperateSoulC).wOption);
        }
        if (hasNewMobStat(Ember)) {
            outPacket.encodeInt(getNewOptionsByMobStat(Ember).nOption);
            outPacket.encodeInt(getNewOptionsByMobStat(Ember).rOption);
            outPacket.encodeInt(getNewOptionsByMobStat(Ember).wOption);
            outPacket.encodeInt(getNewOptionsByMobStat(Ember).tOption / 500);
            outPacket.encodeInt(getNewOptionsByMobStat(Ember).uOption);
        }
        if (hasNewMobStat(TrueSight)) {
            outPacket.encodeInt(getNewOptionsByMobStat(TrueSight).nOption);
            outPacket.encodeInt(getNewOptionsByMobStat(TrueSight).rOption);
            outPacket.encodeInt(getNewOptionsByMobStat(TrueSight).tOption / 500);
            outPacket.encodeInt(getNewOptionsByMobStat(TrueSight).cOption);
            outPacket.encodeInt(getNewOptionsByMobStat(TrueSight).pOption);
            outPacket.encodeInt(getNewOptionsByMobStat(TrueSight).uOption);
            outPacket.encodeInt(getNewOptionsByMobStat(TrueSight).wOption);
        }
        if (hasNewMobStat(MultiDamSkill)) {
            outPacket.encodeInt(getNewOptionsByMobStat(MultiDamSkill).cOption);
        }
        if (hasNewMobStat(Laser)) {
            outPacket.encodeInt(getNewOptionsByMobStat(Laser).nOption);
            outPacket.encodeInt(getNewOptionsByMobStat(Laser).rOption);
            outPacket.encodeInt(getNewOptionsByMobStat(Laser).tOption / 500);
            outPacket.encodeInt(getNewOptionsByMobStat(Laser).wOption);
            outPacket.encodeInt(getNewOptionsByMobStat(Laser).uOption);
        }
        if (hasNewMobStat(ElementResetBySummon)) {
            outPacket.encodeInt(getNewOptionsByMobStat(ElementResetBySummon).cOption);
            outPacket.encodeInt(getNewOptionsByMobStat(ElementResetBySummon).pOption);
            outPacket.encodeInt(getNewOptionsByMobStat(ElementResetBySummon).uOption);
            outPacket.encodeInt(getNewOptionsByMobStat(ElementResetBySummon).wOption);
        }
        if (hasNewMobStat(BahamutLightElemAddDam)) {
            outPacket.encodeInt(getNewOptionsByMobStat(BahamutLightElemAddDam).pOption);
            outPacket.encodeInt(getNewOptionsByMobStat(BahamutLightElemAddDam).cOption);
        }
        getNewStatVals().clear();
    }

    private int[] getMaskByCollection(Map<MobStat, Option> map) {
        int[] res = new int[MobStat.MAX_LENGTH];
        for (MobStat mobStat : map.keySet()) {
            res[mobStat.getPosition()] |= mobStat.getValue();
        }
        OutPacket outPacket = new OutPacket();
        for (int re : res) {
            outPacket.encodeInt(re);
        }
        return res;
    }

    public int[] getNewMask() {
        return getMaskByCollection(getNewStatVals());
    }

    public int[] getCurrentMask() {
        return getMaskByCollection(getCurrentStatVals());
    }

    public int[] getRemovedMask() {
        return getMaskByCollection(getRemovedStatVals());
    }

    public boolean hasNewMobStat(MobStat mobStat) {
        return getNewStatVals().keySet().contains(mobStat);
    }

    public boolean hasCurrentMobStat(MobStat mobStat) {
        return getCurrentStatVals().keySet().contains(mobStat);
    }

    public boolean hasBurnFromSkill(int skillID) {
        return getBurnBySkill(skillID) != null;
    }

    public BurnedInfo getBurnBySkill(int skillID) {
        BurnedInfo res = null;
        for (BurnedInfo bi : getBurnedInfo()) {
            if (bi.getSkillId() == skillID) {
                res = bi;
            }
        }
        return res; // wow no lambda for once
    }

    public boolean hasRemovedMobStat(MobStat mobStat) {
        return getRemovedStatVals().keySet().contains(mobStat);
    }

    public void removeMobStat(MobStat mobStat, Boolean fromSchedule) {
        getRemovedStatVals().put(mobStat, getCurrentStatVals().get(mobStat));
        getCurrentStatVals().remove(mobStat);
        getMob().getField().broadcastPacket(new MobStatReset(getMob(), (byte) 1, false));
        getSchedules().remove(mobStat);
        if (!fromSchedule && getSchedules().containsKey(mobStat)) {
            getSchedules().get(mobStat).cancel(true);
            getSchedules().remove(mobStat);
        } else {
            getSchedules().remove(mobStat);
        }
    }

    public void removeBurnedInfo(Integer charId, Boolean fromSchedule) {
        List<BurnedInfo> biList = getBurnedInfo()
                .stream().filter(bi -> bi.getCharacterId() == charId)
                .collect(Collectors.toList());
        getBurnedInfo().removeAll(biList);
        getRemovedStatVals().put(BurnedInfo, getCurrentOptionsByMobStat(BurnedInfo));
        if (getBurnedInfo().size() == 0) {
            getCurrentStatVals().remove(BurnedInfo);
        }
        getMob().getField().broadcastPacket(new MobStatReset(getMob(), (byte) 1, false, biList));
        if (!fromSchedule) {
            getBurnCancelSchedules().get(charId).cancel(true);
            getBurnCancelSchedules().remove(charId);
            getBurnSchedules().get(charId).cancel(true);
            getBurnSchedules().remove(charId);
        } else {
            getBurnCancelSchedules().remove(charId);
            getBurnSchedules().remove(charId);
        }
    }

    /**
     * Adds a new MobStat to this MobTemporaryStat. Will immediately broadcast the reaction to all clients.
     * Only works for user skills, not mob skills. For the latter, use {@link #addMobSkillOptionsAndBroadCast(MobStat, Option)}.
     *
     * @param mobStat The MobStat to add.
     * @param option  The Option that contains the values of the stat.
     */
    public void addStatOptionsAndBroadcast(MobStat mobStat, Option option) {
        addStatOptions(mobStat, option);
        mob.getField().broadcastPacket(new MobStatSet(getMob(), (short) 0));
    }

    /**
     * Adds a new MobStat to this MobTemporary stat. Will immediately broadcast the reaction to all clients.
     * Only works for mob skills, not user skills. For the latter, use {@link #addStatOptionsAndBroadcast(MobStat, Option)}.
     *
     * @param mobStat The MobStat to add.
     * @param o       The option that contains the values of the stat.
     */
    public void addMobSkillOptionsAndBroadCast(MobStat mobStat, Option o) {
        o.rOption |= o.slv << 16; // mob skills are encoded differently: not an int, but short (skills ID), then short (slv).
        addStatOptionsAndBroadcast(mobStat, o);
    }

    public void addStatOptions(MobStat mobStat, Option option) {
        option.tTerm *= 1000;
        option.tOption *= 1000;
        int tAct = option.tOption > 0 ? option.tOption : option.tTerm;
        getNewStatVals().put(mobStat, option);
        getCurrentStatVals().put(mobStat, option);
        if (tAct > 0 && mobStat != BurnedInfo) {
            if (getSchedules().containsKey(mobStat)) {
                getSchedules().get(mobStat).cancel(true);
            }
            ScheduledFuture sf = EventManager.getInstance().addEvent(() -> removeMobStat(mobStat, true), tAct);
            getSchedules().put(mobStat, sf);
        }
    }

    public boolean hasNewMovementAffectingStat() {
        return getNewStatVals()
                .keySet()
                .stream()
                .anyMatch(MobStat::isMovementAffectingStat);
    }

    public boolean hasCurrentMovementAffectingStat() {
        return getCurrentStatVals()
                .keySet()
                .stream()
                .anyMatch(MobStat::isMovementAffectingStat);
    }

    public boolean hasRemovedMovementAffectingStat() {
        return getRemovedStatVals()
                .keySet()
                .stream()
                .anyMatch(MobStat::isMovementAffectingStat);
    }

    public void clear() {
        for (ScheduledFuture t : getBurnSchedules().values()) {
            t.cancel(true);
        }
        getBurnSchedules().clear();
        for (ScheduledFuture t : getBurnCancelSchedules().values()) {
            t.cancel(true);
        }
        getBurnCancelSchedules().clear();
        for (ScheduledFuture t : getSchedules().values()) {
            t.cancel(true);
        }
        getSchedules().clear();
        getCurrentStatVals().forEach((ms, o) -> removeMobStat(ms, false));
    }

    public void createAndAddBurnedInfo(int charId, Skill skill, int max) {
        BurnedInfo bu = getBurnedInfo().stream().
                filter(b -> b.getSkillId() == skill.getId() && b.getCharacterId() == charId)
                .findFirst().orElse(null);
        SkillInfo si = SkillData.getInstance().getSkillInfoById(skill.getSkillId());
        int slv = skill.getCurrentLevel();
        BurnedInfo bi = new BurnedInfo();
        bi.setCharacterId(charId);
        bi.setSkillId(skill.getSkillId());
        bi.setDamage(si.getValue(dot, slv));
        bi.setInterval(si.getValue(dotInterval, slv) * 1000);
        int time = si.getValue(dotTime, slv) * 1000;
        bi.setEnd((int) (System.currentTimeMillis() + time));
        bi.setDotCount(time / bi.getInterval());
        bi.setSuperPos(si.getValue(dotSuperpos, slv));
        bi.setAttackDelay(0);
        bi.setDotTickIdx(0);
        bi.setDotTickDamR(si.getValue(dot, slv));
        bi.setDotAnimation(bi.getAttackDelay() + bi.getInterval() + time);
        bi.setStartTime((int) System.currentTimeMillis());
        bi.setLastUpdate((int) System.currentTimeMillis());
        if (bu != null) {
            removeBurnedInfo(charId, false);
        }
        getBurnedInfo().add(bi);
        addStatOptionsAndBroadcast(MobStat.BurnedInfo, new Option());
        ScheduledFuture sf = EventManager.getInstance().addEvent(() -> removeBurnedInfo(charId, true), time);
        ScheduledFuture burn = EventManager.getInstance().addFixedRateEvent(
                () -> getMob().damage((long) bi.getDamage()), 0, bi.getInterval(), bi.getDotCount());
        getBurnCancelSchedules().put(charId, sf);
        getBurnSchedules().put(charId, burn);
    }

}