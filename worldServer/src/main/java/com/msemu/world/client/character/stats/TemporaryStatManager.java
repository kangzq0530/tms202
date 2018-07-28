package com.msemu.world.client.character.stats;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.thread.EventManager;
import com.msemu.commons.utils.types.Tuple;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_TemporaryStatReset;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_TemporaryStatSet;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.skill.LarknessManager;
import com.msemu.world.client.field.AffectedArea;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/11.
 */

public class TemporaryStatManager {
    private static final Logger log = LoggerFactory.getLogger(TemporaryStatManager.class);
    @Getter
    private Map<CharacterTemporaryStat, List<Option>> currentStats = new HashMap<>();
    @Getter
    private Map<CharacterTemporaryStat, List<Option>> newStats = new HashMap<>();
    @Getter
    private Map<CharacterTemporaryStat, List<Option>> removedStats = new HashMap<>();
    @Getter
    private Map<CharacterTemporaryStat, ScheduledFuture> schedules = new HashMap<>();
    @Getter
    private HashMap<Tuple<CharacterTemporaryStat, Option>, ScheduledFuture> indieSchedules = new HashMap<>();
    @Getter
    @Setter
    private int pvpDamage;
    @Getter
    @Setter
    private byte defenseState;
    @Getter
    @Setter
    private byte defenseAtt;
    @Getter
    private int[] diceInfo = new int[22];
    @Getter
    @Setter
    private List<Integer> mobZoneStates;
    @Getter
    @Setter
    private int viperEnergyCharge;
    @Getter
    @Setter
    private StopForceAtom stopForceAtom = new StopForceAtom();
    @Getter
    @Setter
    private LarknessManager larknessManager;
    @Getter
    private Character character;
    @Getter
    private List<TemporaryStatBase> twoStates = new ArrayList<>();
    @Getter
    private Set<AffectedArea> affectedAreas = new HashSet<>();

    public TemporaryStatManager(Character character) {
        this.character = character;
        for (CharacterTemporaryStat cts : TSIndex.getAllCTS()) {
            switch (cts) {
                case PartyBooster:
                    twoStates.add(new PartyBooster());
                    break;
                case GuidedBullet:
                    twoStates.add(new GuidedBullet());
                    break;
                case EnergyCharged:
                    twoStates.add(new TemporaryStatBase(false));
                    break;
                case RideVehicleExpire:
                case RideVehicle:
                    twoStates.add(new TwoStateTemporaryStat(false));
                    break;
                default:
                    twoStates.add(new TwoStateTemporaryStat(true));
                    break;
            }
        }
    }

    public TemporaryStatBase getTSBByTSIndex(TSIndex tsi) {
        return getTwoStates().get(tsi.getIndex());
    }

    public void putCharacterStatValue(CharacterTemporaryStat cts, Option option) {
        boolean indie = cts.isIndie();
        option.tOption *= 1000;
        option.tTerm *= 1000;
        if (cts == CharacterTemporaryStat.CombatOrders) {
            character.setCombatOrders(option.nOption);
        }
        if (!indie) {
            List<Option> optList = new ArrayList<>();
            optList.add(option);
            getNewStats().put(cts, optList);
            getCurrentStats().put(cts, optList);
            if (option.tOption > 0) {
                if (getSchedules().containsKey(cts)) {
                    getSchedules().get(cts).cancel(false);
                }
                ScheduledFuture sf = EventManager.getInstance().addEvent(() -> removeStat(cts, true), option.tOption);
                getSchedules().put(cts, sf);
            }
        } else {
            List<Option> optList;
            if (hasStat(cts)) {
                optList = getCurrentStats().get(cts);
            } else {
                optList = new ArrayList<>();
            }
            if (optList.contains(option)) {
                optList.remove(getOptByCTSAndSkill(cts, option.nReason));
            }
            optList.add(option);
            getNewStats().put(cts, optList);
            getCurrentStats().put(cts, optList);
            if (option.tTerm > 0) {
                Tuple<CharacterTemporaryStat, Option> tuple = new Tuple<>(cts, option);
                if (getIndieSchedules().containsKey(tuple)) {
                    getIndieSchedules().get(tuple).cancel(false);
                }
                ScheduledFuture sf = EventManager.getInstance().addEvent(() -> removeIndieStat(cts, option, true), option.tTerm);
                getIndieSchedules().put(tuple, sf);
            }
        }
    }

    public synchronized void removeIndieStat(CharacterTemporaryStat cts, Option option, Boolean fromSchedule) {
        List<Option> optList = new ArrayList<>();
        optList.add(option);
        getRemovedStats().put(cts, optList);
        if (getCurrentStats().containsKey(cts)) {
            getCurrentStats().get(cts).remove(option);
            if (getCurrentStats().get(cts).size() == 0) {
                getCurrentStats().remove(cts);
            }
        }
        getCharacter().getClient().write(new LP_TemporaryStatReset(this, false));
        Tuple<CharacterTemporaryStat, Option> tuple = new Tuple<>(cts, option);
        if (!fromSchedule && getIndieSchedules().containsKey(tuple)) {
            getIndieSchedules().get(tuple).cancel(false);
        } else {
            getIndieSchedules().remove(tuple);
        }
    }

    public Option getOptByCTSAndSkill(CharacterTemporaryStat cts, int skillID) {
        Option res = null;
        if (getCurrentStats().containsKey(cts)) {
            for (Option o : getCurrentStats().get(cts)) {
                if (o.rOption == skillID || o.nReason == skillID) {
                    res = o;
                    break;
                }
            }
        }
        return res;
    }

    public synchronized void removeStat(CharacterTemporaryStat cts, Boolean fromSchedule) {
        if (cts == CharacterTemporaryStat.CombatOrders) {
            character.setCombatOrders(0);
        }
        getRemovedStats().put(cts, getCurrentStats().get(cts));
        getCurrentStats().remove(cts);
        getCharacter().getClient().write(new LP_TemporaryStatReset(this, false));
        if (TSIndex.isTwoStat(cts)) {
            getTSBByTSIndex(TSIndex.getTSEFromCTS(cts)).reset();
        }
        if (!fromSchedule && getSchedules().containsKey(cts)) {
            getSchedules().get(cts).cancel(false);
        } else {
            getSchedules().remove(cts);
        }
    }

    public boolean hasNewStat(CharacterTemporaryStat cts) {
        return newStats.containsKey(cts);
    }

    public boolean hasStat(CharacterTemporaryStat cts) {
        return getCurrentStats().containsKey(cts);
    }

    public Option getOption(CharacterTemporaryStat cts) {
        if (hasStat(cts)) {
            return getCurrentStats().get(cts).get(0);
        }
        return new Option();
    }

    public List<Option> getOptions(CharacterTemporaryStat cts) {
        if (hasStat(cts)) {
            return getCurrentStats().get(cts);
        }
        List<Option> res = new ArrayList<>();
        return res;
    }

    public boolean hasNewMovingEffectingStat() {
        return getNewStats().keySet().stream().anyMatch(CharacterTemporaryStat::isMovingEffectingStat);
    }

    public boolean hasMovingEffectingStat() {
        return getCurrentStats().keySet().stream().anyMatch(CharacterTemporaryStat::isMovingEffectingStat);
    }

    public boolean hasRemovedMovingEffectingStat() {
        return getRemovedStats().keySet().stream().anyMatch(CharacterTemporaryStat::isMovingEffectingStat);
    }


    public void sendSetStatPacket() {
        getCharacter().getClient().write(new LP_TemporaryStatSet(this));
    }

    public void sendResetStatPacket() {
        getCharacter().getClient().write(new LP_TemporaryStatReset(this, false));
    }


    public void addAffectedArea(AffectedArea aa) {
        getAffectedAreas().add(aa);
    }

    public void removeAffectedArea(AffectedArea aa) {
        getAffectedAreas().remove(aa);
        removeStatsBySkill(aa.getSkillID());
    }

    public boolean hasAffectedArea(AffectedArea affectedArea) {
        return getAffectedAreas().contains(affectedArea);
    }

    public void removeStatsBySkill(int skillId) {
        Map<CharacterTemporaryStat, Option> removedMap = new HashMap<>();
        for (CharacterTemporaryStat cts : getCurrentStats().keySet()) {
            Option checkOpt = new Option(skillId);
            if (cts.isIndie() && getOptions(cts).contains(checkOpt)) {
                Option o = getOptions(cts).stream().filter(opt -> opt.equals(checkOpt)).findFirst().orElse(null);
                if (o == null) {
                    log.error("Found option null, yet the options contained it?");
                } else {
                    removedMap.put(cts, o);
                }
            } else if (getOption(cts).rOption == skillId || getOption(cts).nReason == skillId) {
                removedMap.put(cts, getOption(cts));
            }
        }
        removedMap.forEach((cts, opt) -> {
            if (cts.isIndie()) {
                removeIndieStat(cts, opt, false);
            } else {
                removeStat(cts, false);
            }
        });
    }

    public int[] getMaskByCollection(Map<CharacterTemporaryStat, List<Option>> map) {
        int[] res = new int[CharacterTemporaryStat.SIZE];
        for (int i = 0; i < res.length; i++) {
            for (CharacterTemporaryStat cts : map.keySet()) {
                if (cts.getPosition() == i) {
                    res[i] |= cts.getValue();
                }
            }
        }
        return res;
    }


    public int[] getCurrentFlags() {
        return getMaskByCollection(getCurrentStats());
    }

    public int[] getNewFlags() {
        return getMaskByCollection(getNewStats());
    }

    public int[] getRemovedFlags() {
        return getMaskByCollection(getRemovedStats());
    }

    public void encodeForLocal(OutPacket outPacket) {
        int[] mask = getNewFlags();
        for (int i = 0; i < getNewFlags().length; i++) {
            outPacket.encodeInt(mask[i]);
        }
        if (hasNewStat(CharacterTemporaryStat.STR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.STR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.STR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.STR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.INT)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.INT).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.INT).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.INT).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DEX)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DEX).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DEX).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DEX).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.LUK)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.LUK).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.LUK).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.LUK).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PAD)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PAD).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PAD).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PAD).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PDD)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PDD).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PDD).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PDD).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MAD)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MAD).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MAD).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MAD).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ACC)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ACC).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ACC).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ACC).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.EVA)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.EVA).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EVA).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EVA).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.EVAR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.EVAR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EVAR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EVAR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Craft)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Craft).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Craft).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Craft).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Speed)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Speed).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Speed).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Speed).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Jump)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Jump).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Jump).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Jump).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.EMHP)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.EMHP).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EMHP).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EMHP).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.EMMP)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.EMMP).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EMMP).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EMMP).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.EPAD)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.EPAD).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EPAD).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EPAD).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.EMAD)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.EMAD).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EMAD).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EMAD).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.EPDD)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.EPDD).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EPDD).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EPDD).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MagicGuard)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MagicGuard).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MagicGuard).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MagicGuard).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DarkSight)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DarkSight).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DarkSight).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DarkSight).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Booster)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Booster).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Booster).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Booster).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PowerGuard)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PowerGuard).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PowerGuard).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PowerGuard).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Guard)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Guard).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Guard).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Guard).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MaxHP)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MaxHP).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MaxHP).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MaxHP).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MaxMP)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MaxMP).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MaxMP).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MaxMP).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Invincible)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Invincible).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Invincible).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Invincible).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SoulArrow)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SoulArrow).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulArrow).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulArrow).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Stun)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Stun).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Stun).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Stun).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Shock)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Shock).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Shock).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Shock).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Poison)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Poison).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Poison).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Poison).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Seal)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Seal).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Seal).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Seal).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Darkness)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Darkness).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Darkness).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Darkness).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ComboCounter)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ComboCounter).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboCounter).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboCounter).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_78)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_78).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_78).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_78).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.WeaponCharge)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.WeaponCharge).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.WeaponCharge).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.WeaponCharge).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_80)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_80).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_80).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_80).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_79)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_79).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_79).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_79).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ElementalCharge)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ElementalCharge).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ElementalCharge).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ElementalCharge).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HolySymbol)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HolySymbol).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HolySymbol).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HolySymbol).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MesoUp)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MesoUp).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MesoUp).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MesoUp).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ShadowPartner)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ShadowPartner).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ShadowPartner).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ShadowPartner).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PickPocket)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PickPocket).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PickPocket).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PickPocket).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MesoGuard)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MesoGuard).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MesoGuard).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MesoGuard).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Thaw)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Thaw).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Thaw).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Thaw).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Weakness)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Weakness).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Weakness).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Weakness).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.WeaknessMdamage)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.WeaknessMdamage).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.WeaknessMdamage).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.WeaknessMdamage).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Curse)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Curse).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Curse).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Curse).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Slow)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Slow).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Slow).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Slow).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.TimeBomb)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.TimeBomb).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TimeBomb).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TimeBomb).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BuffLimit)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BuffLimit).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BuffLimit).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BuffLimit).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Team)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Team).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Team).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Team).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Disorder)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Disorder).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Disorder).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Disorder).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Thread)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Thread).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Thread).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Thread).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Morph)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Morph).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Morph).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Morph).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Ghost)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Ghost).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Ghost).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Ghost).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Regen)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Regen).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Regen).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Regen).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BasicStatUp)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BasicStatUp).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BasicStatUp).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BasicStatUp).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Stance)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Stance).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Stance).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Stance).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SharpEyes)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SharpEyes).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SharpEyes).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SharpEyes).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ManaReflection)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ManaReflection).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ManaReflection).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ManaReflection).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Attract)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Attract).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Attract).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Attract).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Magnet)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Magnet).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Magnet).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Magnet).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MagnetArea)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MagnetArea).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MagnetArea).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MagnetArea).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_249)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_249).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_249).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_249).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_250)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_250).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_250).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_250).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.NoBulletConsume)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.NoBulletConsume).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NoBulletConsume).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NoBulletConsume).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.StackBuff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.StackBuff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.StackBuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.StackBuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Trinity)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Trinity).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Trinity).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Trinity).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Infinity)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Infinity).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Infinity).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Infinity).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AdvancedBless)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AdvancedBless).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AdvancedBless).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AdvancedBless).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IllusionStep)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IllusionStep).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IllusionStep).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IllusionStep).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Blind)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Blind).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Blind).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Blind).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Concentration)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Concentration).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Concentration).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Concentration).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BanMap)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BanMap).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BanMap).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BanMap).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MaxLevelBuff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MaxLevelBuff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MaxLevelBuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MaxLevelBuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Barrier)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Barrier).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Barrier).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Barrier).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DojangShield)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DojangShield).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DojangShield).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DojangShield).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ReverseInput)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ReverseInput).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ReverseInput).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ReverseInput).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MesoUpByItem)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MesoUpByItem).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MesoUpByItem).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MesoUpByItem).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_107)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_107).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_107).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_107).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_108)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_108).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_108).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_108).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ItemUpByItem)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ItemUpByItem).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ItemUpByItem).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ItemUpByItem).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RespectPImmune)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RespectPImmune).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RespectPImmune).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RespectPImmune).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RespectMImmune)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RespectMImmune).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RespectMImmune).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RespectMImmune).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DefenseAtt)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DefenseAtt).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DefenseAtt).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DefenseAtt).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DefenseState)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DefenseState).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DefenseState).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DefenseState).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DojangBerserk)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DojangBerserk).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DojangBerserk).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DojangBerserk).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DojangInvincible)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DojangInvincible).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DojangInvincible).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DojangInvincible).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SoulMasterFinal)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SoulMasterFinal).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulMasterFinal).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulMasterFinal).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.WindBreakerFinal)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.WindBreakerFinal).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.WindBreakerFinal).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.WindBreakerFinal).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ElementalReset)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ElementalReset).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ElementalReset).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ElementalReset).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HideAttack)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HideAttack).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HideAttack).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HideAttack).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.EventRate)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.EventRate).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EventRate).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EventRate).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ComboAbilityBuff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ComboAbilityBuff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboAbilityBuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboAbilityBuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ComboDrain)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ComboDrain).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboDrain).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboDrain).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ComboBarrier)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ComboBarrier).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboBarrier).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboBarrier).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PartyBarrier)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PartyBarrier).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PartyBarrier).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PartyBarrier).tOption);
        }

        // 以下有問題
        if (hasNewStat(CharacterTemporaryStat.BodyPressure)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BodyPressure).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BodyPressure).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BodyPressure).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RepeatEffect)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RepeatEffect).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RepeatEffect).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RepeatEffect).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.ExpBuffRate)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ExpBuffRate).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ExpBuffRate).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ExpBuffRate).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.StopPortion)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.StopPortion).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.StopPortion).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.StopPortion).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.StopMotion)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.StopMotion).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.StopMotion).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.StopMotion).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Fear)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Fear).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Fear).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Fear).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MagicShield)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MagicShield).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MagicShield).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MagicShield).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MagicResistance)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MagicResistance).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MagicResistance).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MagicResistance).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SoulStone)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SoulStone).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulStone).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulStone).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Flying)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Flying).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Flying).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Flying).tOption);
        }
        // 以上有問題
        if (hasNewStat(CharacterTemporaryStat.NewFlying)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.NewFlying).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NewFlying).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NewFlying).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.NaviFlying)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NaviFlying).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NaviFlying).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NaviFlying).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Frozen)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Frozen).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Frozen).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Frozen).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Frozen2)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Frozen2).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Frozen2).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Frozen2).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Web)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Web).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Web).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Web).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Enrage)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Enrage).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Enrage).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Enrage).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.NotDamaged)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.NotDamaged).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NotDamaged).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NotDamaged).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.FinalCut)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.FinalCut).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FinalCut).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FinalCut).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HowlingAttackDamage)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HowlingAttackDamage).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HowlingAttackDamage).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HowlingAttackDamage).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BeastFormDamageUp)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BeastFormDamageUp).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BeastFormDamageUp).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BeastFormDamageUp).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Dance)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Dance).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Dance).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Dance).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Cyclone)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Cyclone).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Cyclone).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Cyclone).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.OnCapsule)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.OnCapsule).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.OnCapsule).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.OnCapsule).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HowlingCritical)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HowlingCritical).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HowlingCritical).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HowlingCritical).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HowlingMaxMP)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HowlingMaxMP).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HowlingMaxMP).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HowlingMaxMP).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HowlingDefence)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HowlingDefence).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HowlingDefence).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HowlingDefence).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HowlingEvasion)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HowlingEvasion).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HowlingEvasion).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HowlingEvasion).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Conversion)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Conversion).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Conversion).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Conversion).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Revive)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Revive).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Revive).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Revive).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PinkbeanMinibeenMove)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PinkbeanMinibeenMove).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PinkbeanMinibeenMove).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PinkbeanMinibeenMove).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Sneak)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Sneak).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Sneak).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Sneak).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Mechanic)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Mechanic).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Mechanic).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Mechanic).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DrawBack)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DrawBack).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DrawBack).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DrawBack).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BeastFormMaxHP)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BeastFormMaxHP).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BeastFormMaxHP).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BeastFormMaxHP).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Dice)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Dice).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Dice).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Dice).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BlessingArmor)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BlessingArmor).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BlessingArmor).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BlessingArmor).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BlessingArmorIncPAD)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BlessingArmorIncPAD).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BlessingArmorIncPAD).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BlessingArmorIncPAD).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DamR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DamR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DamR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DamR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.TeleportMasteryOn)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.TeleportMasteryOn).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TeleportMasteryOn).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TeleportMasteryOn).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.CombatOrders)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.CombatOrders).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CombatOrders).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CombatOrders).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Beholder)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Beholder).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Beholder).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Beholder).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DispelItemOption)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DispelItemOption).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DispelItemOption).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DispelItemOption).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DispelItemOptionByField)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DispelItemOptionByField).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DispelItemOptionByField).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DispelItemOptionByField).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Inflation)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Inflation).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Inflation).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Inflation).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.OnixDivineProtection)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.OnixDivineProtection).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.OnixDivineProtection).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.OnixDivineProtection).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Bless)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Bless).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Bless).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Bless).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Explosion)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Explosion).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Explosion).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Explosion).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DarkTornado)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DarkTornado).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DarkTornado).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DarkTornado).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IncMaxHP)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IncMaxHP).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncMaxHP).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncMaxHP).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IncMaxMP)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IncMaxMP).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncMaxMP).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncMaxMP).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PVPDamage)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PVPDamage).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PVPDamage).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PVPDamage).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PVPDamageSkill)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PVPDamageSkill).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PVPDamageSkill).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PVPDamageSkill).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PvPScoreBonus)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PvPScoreBonus).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PvPScoreBonus).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PvPScoreBonus).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PvPInvincible)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PvPInvincible).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PvPInvincible).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PvPInvincible).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PvPRaceEffect)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PvPRaceEffect).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PvPRaceEffect).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PvPRaceEffect).tOption);
        }
//        if (hasNewStat(IceKnight)) {
//            outPacket.encodeShort(getOption(IceKnight).nOption);
//            outPacket.encodeInt(getOption(IceKnight).rOption);
//            outPacket.encodeInt(getOption(IceKnight).tOption);
//        }
        if (hasNewStat(CharacterTemporaryStat.HolyMagicShell)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HolyMagicShell).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HolyMagicShell).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HolyMagicShell).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.InfinityForce)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.InfinityForce).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.InfinityForce).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.InfinityForce).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AmplifyDamage)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AmplifyDamage).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AmplifyDamage).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AmplifyDamage).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.KeyDownTimeIgnore)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.KeyDownTimeIgnore).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KeyDownTimeIgnore).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KeyDownTimeIgnore).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MasterMagicOn)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MasterMagicOn).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MasterMagicOn).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MasterMagicOn).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AsrR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AsrR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AsrR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AsrR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AsrRByItem)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AsrRByItem).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AsrRByItem).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AsrRByItem).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.TerR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.TerR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TerR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TerR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DamAbsorbShield)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DamAbsorbShield).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DamAbsorbShield).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DamAbsorbShield).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Roulette)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Roulette).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Roulette).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Roulette).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Event)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Event).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Event).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Event).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SpiritLink)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SpiritLink).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SpiritLink).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SpiritLink).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.CriticalBuff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.CriticalBuff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CriticalBuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CriticalBuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DropRate)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DropRate).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DropRate).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DropRate).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PlusExpRate)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PlusExpRate).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PlusExpRate).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PlusExpRate).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ItemInvincible)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ItemInvincible).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ItemInvincible).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ItemInvincible).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ItemCritical)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ItemCritical).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ItemCritical).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ItemCritical).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ItemEvade)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ItemEvade).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ItemEvade).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ItemEvade).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Event2)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Event2).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Event2).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Event2).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.VampiricTouch)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.VampiricTouch).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.VampiricTouch).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.VampiricTouch).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DDR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DDR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DDR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DDR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IncCriticalDamMin)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IncCriticalDamMin).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncCriticalDamMin).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncCriticalDamMin).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IncCriticalDamMax)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IncCriticalDamMax).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncCriticalDamMax).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncCriticalDamMax).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IncTerR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IncTerR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncTerR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncTerR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IncAsrR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IncAsrR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncAsrR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncAsrR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DeathMark)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DeathMark).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DeathMark).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DeathMark).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PainMark)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PainMark).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PainMark).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PainMark).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.UsefulAdvancedBless)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.UsefulAdvancedBless).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.UsefulAdvancedBless).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.UsefulAdvancedBless).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Lapidification)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Lapidification).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Lapidification).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Lapidification).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.VampDeath)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.VampDeath).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.VampDeath).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.VampDeath).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.VampDeathSummon)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.VampDeathSummon).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.VampDeathSummon).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.VampDeathSummon).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.VenomSnake)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.VenomSnake).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.VenomSnake).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.VenomSnake).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.CarnivalAttack)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.CarnivalAttack).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CarnivalAttack).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CarnivalAttack).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.CarnivalDefence)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CarnivalDefence).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CarnivalDefence).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CarnivalDefence).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.CarnivalExp)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.CarnivalExp).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CarnivalExp).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CarnivalExp).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SlowAttack)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SlowAttack).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SlowAttack).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SlowAttack).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PyramidEffect)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PyramidEffect).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PyramidEffect).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PyramidEffect).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HollowPointBullet)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HollowPointBullet).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HollowPointBullet).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HollowPointBullet).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.KeyDownMoving)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.KeyDownMoving).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KeyDownMoving).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KeyDownMoving).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.KeyDownAreaMoving)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.KeyDownAreaMoving).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KeyDownAreaMoving).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KeyDownAreaMoving).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.CygnusElementSkill)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.CygnusElementSkill).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CygnusElementSkill).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CygnusElementSkill).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IgnoreTargetDEF)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IgnoreTargetDEF).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnoreTargetDEF).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnoreTargetDEF).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Invisible)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Invisible).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Invisible).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Invisible).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ReviveOnce)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ReviveOnce).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ReviveOnce).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ReviveOnce).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AntiMagicShell)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AntiMagicShell).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AntiMagicShell).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AntiMagicShell).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.EnrageCr)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.EnrageCr).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EnrageCr).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EnrageCr).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.EnrageCrDamMin)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.EnrageCrDamMin).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EnrageCrDamMin).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EnrageCrDamMin).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BlessOfDarkness)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BlessOfDarkness).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BlessOfDarkness).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BlessOfDarkness).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.LifeTidal)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.LifeTidal).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.LifeTidal).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.LifeTidal).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Judgement)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Judgement).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Judgement).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Judgement).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DojangLuckyBonus)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DojangLuckyBonus).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DojangLuckyBonus).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DojangLuckyBonus).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HitCriDamR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HitCriDamR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HitCriDamR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HitCriDamR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Larkness)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Larkness).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Larkness).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Larkness).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SmashStack)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SmashStack).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SmashStack).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SmashStack).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ReshuffleSwitch)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ReshuffleSwitch).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ReshuffleSwitch).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ReshuffleSwitch).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SpecialAction)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SpecialAction).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SpecialAction).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SpecialAction).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ArcaneAim)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ArcaneAim).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ArcaneAim).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ArcaneAim).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.StopForceAtomInfo)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.StopForceAtomInfo).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.StopForceAtomInfo).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.StopForceAtomInfo).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SoulGazeCriDamR)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulGazeCriDamR).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulGazeCriDamR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulGazeCriDamR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SoulRageCount)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SoulRageCount).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulRageCount).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulRageCount).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PowerTransferGauge)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PowerTransferGauge).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PowerTransferGauge).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PowerTransferGauge).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AffinitySlug)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AffinitySlug).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AffinitySlug).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AffinitySlug).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SoulExalt)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SoulExalt).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulExalt).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulExalt).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HiddenPieceOn)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HiddenPieceOn).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HiddenPieceOn).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HiddenPieceOn).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BossShield)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BossShield).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BossShield).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BossShield).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MobZoneState)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MobZoneState).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MobZoneState).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MobZoneState).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.GiveMeHeal)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.GiveMeHeal).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.GiveMeHeal).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.GiveMeHeal).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.TouchMe)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.TouchMe).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TouchMe).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TouchMe).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Contagion)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Contagion).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Contagion).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Contagion).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ComboUnlimited)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ComboUnlimited).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboUnlimited).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboUnlimited).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IgnorePCounter)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IgnorePCounter).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnorePCounter).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnorePCounter).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IgnoreAllCounter)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IgnoreAllCounter).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnoreAllCounter).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnoreAllCounter).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IgnorePImmune)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IgnorePImmune).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnorePImmune).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnorePImmune).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IgnoreAllImmune)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IgnoreAllImmune).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnoreAllImmune).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnoreAllImmune).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.FinalJudgement)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.FinalJudgement).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FinalJudgement).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FinalJudgement).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_284)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_284).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_284).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_284).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.KnightsAura)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.KnightsAura).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KnightsAura).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KnightsAura).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IceAura)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IceAura).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IceAura).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IceAura).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.FireAura)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.FireAura).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FireAura).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FireAura).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.VengeanceOfAngel)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.VengeanceOfAngel).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.VengeanceOfAngel).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.VengeanceOfAngel).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HeavensDoor)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HeavensDoor).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HeavensDoor).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HeavensDoor).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Preparation)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Preparation).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Preparation).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Preparation).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BullsEye)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BullsEye).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BullsEye).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BullsEye).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IncEffectHPPotion)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IncEffectHPPotion).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncEffectHPPotion).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncEffectHPPotion).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IncEffectMPPotion)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IncEffectMPPotion).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncEffectMPPotion).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncEffectMPPotion).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SoulMP)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SoulMP).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulMP).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulMP).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.FullSoulMP)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.FullSoulMP).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FullSoulMP).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FullSoulMP).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SoulSkillDamageUp)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SoulSkillDamageUp).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulSkillDamageUp).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulSkillDamageUp).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BleedingToxin)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BleedingToxin).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BleedingToxin).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BleedingToxin).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IgnoreMobDamR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IgnoreMobDamR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnoreMobDamR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnoreMobDamR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Asura)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Asura).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Asura).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Asura).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_296)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_296).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_296).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_296).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.FlipTheCoin)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.FlipTheCoin).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FlipTheCoin).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FlipTheCoin).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.UnityOfPower)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.UnityOfPower).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.UnityOfPower).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.UnityOfPower).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Stimulate)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Stimulate).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Stimulate).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Stimulate).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ReturnTeleport)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ReturnTeleport).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ReturnTeleport).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ReturnTeleport).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.CapDebuff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.CapDebuff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CapDebuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CapDebuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DropRIncrease)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DropRIncrease).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DropRIncrease).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DropRIncrease).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IgnoreMobpdpR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IgnoreMobpdpR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnoreMobpdpR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnoreMobpdpR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BdR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BdR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BdR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BdR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Exceed)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Exceed).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Exceed).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Exceed).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DiabolikRecovery)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DiabolikRecovery).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DiabolikRecovery).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DiabolikRecovery).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.FinalAttackProp)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.FinalAttackProp).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FinalAttackProp).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FinalAttackProp).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ExceedOverload)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ExceedOverload).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ExceedOverload).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ExceedOverload).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DevilishPower)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DevilishPower).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DevilishPower).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DevilishPower).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.OverloadCount)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.OverloadCount).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.OverloadCount).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.OverloadCount).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BuckShot)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BuckShot).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BuckShot).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BuckShot).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.FireBomb)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.FireBomb).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FireBomb).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FireBomb).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HalfstatByDebuff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HalfstatByDebuff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HalfstatByDebuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HalfstatByDebuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SurplusSupply)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SurplusSupply).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SurplusSupply).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SurplusSupply).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SetBaseDamage)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SetBaseDamage).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SetBaseDamage).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SetBaseDamage).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AmaranthGenerator)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AmaranthGenerator).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AmaranthGenerator).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AmaranthGenerator).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.StrikerHyperElectric)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.StrikerHyperElectric).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.StrikerHyperElectric).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.StrikerHyperElectric).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.EventPointAbsorb)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.EventPointAbsorb).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EventPointAbsorb).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EventPointAbsorb).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.EventAssemble)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.EventAssemble).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EventAssemble).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EventAssemble).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.StormBringer)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.StormBringer).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.StormBringer).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.StormBringer).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ACCR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ACCR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ACCR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ACCR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DEXR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DEXR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DEXR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DEXR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Albatross)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Albatross).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Albatross).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Albatross).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Translucence)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Translucence).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Translucence).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Translucence).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PoseType)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PoseType).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PoseType).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PoseType).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.LightOfSpirit)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.LightOfSpirit).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.LightOfSpirit).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.LightOfSpirit).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ElementSoul)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ElementSoul).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ElementSoul).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ElementSoul).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.GlimmeringTime)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.GlimmeringTime).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.GlimmeringTime).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.GlimmeringTime).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Restoration)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Restoration).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Restoration).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Restoration).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ComboCostInc)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ComboCostInc).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboCostInc).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboCostInc).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ChargeBuff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ChargeBuff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ChargeBuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ChargeBuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.TrueSight)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.TrueSight).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TrueSight).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TrueSight).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.CrossOverChain)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.CrossOverChain).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CrossOverChain).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CrossOverChain).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ChillingStep)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ChillingStep).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ChillingStep).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ChillingStep).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Reincarnation)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Reincarnation).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Reincarnation).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Reincarnation).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DotBasedBuff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DotBasedBuff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DotBasedBuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DotBasedBuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BlessEnsenble)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BlessEnsenble).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BlessEnsenble).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BlessEnsenble).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ExtremeArchery)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ExtremeArchery).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ExtremeArchery).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ExtremeArchery).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.QuiverCatridge)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.QuiverCatridge).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.QuiverCatridge).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.QuiverCatridge).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AdvancedQuiver)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AdvancedQuiver).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AdvancedQuiver).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AdvancedQuiver).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.UserControlMob)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.UserControlMob).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.UserControlMob).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.UserControlMob).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ImmuneBarrier)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ImmuneBarrier).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ImmuneBarrier).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ImmuneBarrier).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ArmorPiercing)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ArmorPiercing).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ArmorPiercing).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ArmorPiercing).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ZeroAuraStr)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ZeroAuraStr).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ZeroAuraStr).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ZeroAuraStr).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ZeroAuraSpd)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ZeroAuraSpd).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ZeroAuraSpd).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ZeroAuraSpd).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.CriticalGrowing)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.CriticalGrowing).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CriticalGrowing).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CriticalGrowing).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.QuickDraw)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.QuickDraw).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.QuickDraw).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.QuickDraw).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BowMasterConcentration)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BowMasterConcentration).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BowMasterConcentration).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BowMasterConcentration).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.TimeFastABuff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.TimeFastABuff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TimeFastABuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TimeFastABuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.TimeFastBBuff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.TimeFastBBuff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TimeFastBBuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TimeFastBBuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.GatherDropR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.GatherDropR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.GatherDropR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.GatherDropR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AimBox2D)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AimBox2D).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AimBox2D).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AimBox2D).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.CursorSniping)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.CursorSniping).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CursorSniping).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CursorSniping).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IncMonsterBattleCaptureRate)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IncMonsterBattleCaptureRate).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncMonsterBattleCaptureRate).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncMonsterBattleCaptureRate).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DebuffTolerance)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DebuffTolerance).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DebuffTolerance).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DebuffTolerance).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DotHealHPPerSecond)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DotHealHPPerSecond).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DotHealHPPerSecond).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DotHealHPPerSecond).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SpiritGuard)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SpiritGuard).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SpiritGuard).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SpiritGuard).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PreReviveOnce)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PreReviveOnce).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PreReviveOnce).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PreReviveOnce).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SetBaseDamageByBuff)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SetBaseDamageByBuff).nOption); // isEncode4
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SetBaseDamageByBuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SetBaseDamageByBuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.LimitMP)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.LimitMP).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.LimitMP).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.LimitMP).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ReflectDamR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ReflectDamR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ReflectDamR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ReflectDamR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ComboTempest)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ComboTempest).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboTempest).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComboTempest).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MHPCutR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MHPCutR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MHPCutR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MHPCutR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MMPCutR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MMPCutR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MMPCutR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MMPCutR).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SelfWeakness)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SelfWeakness).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SelfWeakness).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SelfWeakness).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ElementDarkness)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ElementDarkness).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ElementDarkness).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ElementDarkness).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.FlareTrick)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.FlareTrick).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FlareTrick).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FlareTrick).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Ember)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Ember).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Ember).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Ember).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Dominion)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Dominion).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Dominion).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Dominion).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SiphonVitality)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SiphonVitality).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SiphonVitality).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SiphonVitality).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DarknessAscension)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DarknessAscension).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DarknessAscension).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DarknessAscension).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BossWaitingLinesBuff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BossWaitingLinesBuff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BossWaitingLinesBuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BossWaitingLinesBuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DamageReduce)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DamageReduce).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DamageReduce).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DamageReduce).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ShadowServant)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ShadowServant).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ShadowServant).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ShadowServant).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ShadowIllusion)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ShadowIllusion).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ShadowIllusion).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ShadowIllusion).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AddAttackCount)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AddAttackCount).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AddAttackCount).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AddAttackCount).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ComplusionSlant)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ComplusionSlant).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComplusionSlant).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ComplusionSlant).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.JaguarSummoned)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.JaguarSummoned).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.JaguarSummoned).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.JaguarSummoned).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.JaguarCount)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.JaguarCount).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.JaguarCount).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.JaguarCount).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SSFShootingAttack)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.SSFShootingAttack).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SSFShootingAttack).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SSFShootingAttack).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DevilCry)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DevilCry).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DevilCry).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DevilCry).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ShieldAttack)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ShieldAttack).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ShieldAttack).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ShieldAttack).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BMageAura)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BMageAura).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BMageAura).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BMageAura).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DarkLighting)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DarkLighting).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DarkLighting).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DarkLighting).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AttackCountX)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AttackCountX).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AttackCountX).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AttackCountX).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BMageDeath)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BMageDeath).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BMageDeath).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BMageDeath).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BombTime)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BombTime).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BombTime).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BombTime).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.NoDebuff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.NoDebuff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NoDebuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NoDebuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.XenonAegisSystem)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.XenonAegisSystem).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.XenonAegisSystem).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.XenonAegisSystem).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AngelicBursterSoulSeeker)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AngelicBursterSoulSeeker).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AngelicBursterSoulSeeker).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AngelicBursterSoulSeeker).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HiddenPossession)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HiddenPossession).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HiddenPossession).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HiddenPossession).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.NightWalkerBat)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.NightWalkerBat).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NightWalkerBat).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NightWalkerBat).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.NightLordMark)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.NightLordMark).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NightLordMark).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NightLordMark).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.WizardIgnite)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.WizardIgnite).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.WizardIgnite).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.WizardIgnite).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_413)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_413).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_413).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_413).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BattlePvPHelenaMark)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BattlePvPHelenaMark).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPHelenaMark).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPHelenaMark).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BattlePvPHelenaWindSpirit)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BattlePvPHelenaWindSpirit).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPHelenaWindSpirit).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPHelenaWindSpirit).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BattlePvPLangEProtection)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BattlePvPLangEProtection).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPLangEProtection).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPLangEProtection).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BattlePvPLeeMalNyunScaleUp)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BattlePvPLeeMalNyunScaleUp).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPLeeMalNyunScaleUp).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPLeeMalNyunScaleUp).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BattlePvPRevive)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BattlePvPRevive).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPRevive).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPRevive).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PinkbeanAttackBuff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PinkbeanAttackBuff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PinkbeanAttackBuff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PinkbeanAttackBuff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RandAreaAttack)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RandAreaAttack).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RandAreaAttack).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RandAreaAttack).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BattlePvPMikeShield)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BattlePvPMikeShield).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPMikeShield).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPMikeShield).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BattlePvPMikeBugle)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BattlePvPMikeBugle).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPMikeBugle).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPMikeBugle).tOption);
        }
        // 以下好亂QQ
        if (hasNewStat(CharacterTemporaryStat.PinkbeanRelax)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PinkbeanRelax).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PinkbeanRelax).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PinkbeanRelax).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PinkbeanYoYoStack)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PinkbeanYoYoStack).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PinkbeanYoYoStack).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PinkbeanYoYoStack).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.NextAttackEnhance)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.NextAttackEnhance).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NextAttackEnhance).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NextAttackEnhance).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AranBeyonderDamAbsorb)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AranBeyonderDamAbsorb).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AranBeyonderDamAbsorb).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AranBeyonderDamAbsorb).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AranCombotempastOption)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AranCombotempastOption).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AranCombotempastOption).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AranCombotempastOption).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.NautilusFinalAttack)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.NautilusFinalAttack).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NautilusFinalAttack).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NautilusFinalAttack).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ViperTimeLeap)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ViperTimeLeap).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ViperTimeLeap).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ViperTimeLeap).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RoyalGuardState)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RoyalGuardState).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RoyalGuardState).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RoyalGuardState).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RoyalGuardPrepare)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RoyalGuardPrepare).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RoyalGuardPrepare).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RoyalGuardPrepare).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MichaelSoulLink)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MichaelSoulLink).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MichaelSoulLink).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MichaelSoulLink).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MichaelStanceLink)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.MichaelStanceLink).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MichaelStanceLink).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MichaelStanceLink).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.TriflingWhimOnOff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.TriflingWhimOnOff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TriflingWhimOnOff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TriflingWhimOnOff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AddRangeOnOff)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AddRangeOnOff).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AddRangeOnOff).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AddRangeOnOff).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.KinesisPsychicPoint)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.KinesisPsychicPoint).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KinesisPsychicPoint).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KinesisPsychicPoint).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.KinesisPsychicOver)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.KinesisPsychicOver).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KinesisPsychicOver).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KinesisPsychicOver).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.KinesisPsychicShield)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.KinesisPsychicShield).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KinesisPsychicShield).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KinesisPsychicShield).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.KinesisIncMastery)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.KinesisIncMastery).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KinesisIncMastery).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KinesisIncMastery).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.KinesisPsychicEnergeShield)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.KinesisPsychicEnergeShield).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KinesisPsychicEnergeShield).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KinesisPsychicEnergeShield).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BladeStance)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BladeStance).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BladeStance).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BladeStance).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DebuffActiveSkillHPCon)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DebuffActiveSkillHPCon).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DebuffActiveSkillHPCon).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DebuffActiveSkillHPCon).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DebuffIncHP)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.DebuffIncHP).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DebuffIncHP).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DebuffIncHP).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BowMasterMortalBlow)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BowMasterMortalBlow).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BowMasterMortalBlow).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BowMasterMortalBlow).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AngelicBursterSoulResonance)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AngelicBursterSoulResonance).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AngelicBursterSoulResonance).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AngelicBursterSoulResonance).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Fever)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Fever).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Fever).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Fever).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IgnisRore)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IgnisRore).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnisRore).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnisRore).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RpSiksin)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RpSiksin).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RpSiksin).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RpSiksin).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.TeleportMasteryRange)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.TeleportMasteryRange).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TeleportMasteryRange).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.TeleportMasteryRange).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AnimalChange)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AnimalChange).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AnimalChange).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AnimalChange).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_436)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_436).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_436).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_436).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_437)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_437).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_437).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_437).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_433)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_433).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_433).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_433).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IncMaxDamage)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IncMaxDamage).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncMaxDamage).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncMaxDamage).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_434)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_434).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_434).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_434).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_431)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_431).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_431).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_431).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.BlackHeartedCurse)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.BlackHeartedCurse).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BlackHeartedCurse).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BlackHeartedCurse).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.AURA_BOOST)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AURA_BOOST).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AURA_BOOST).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AURA_BOOST).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.HayatoStance)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HayatoStance).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HayatoStance).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HayatoStance).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.HayatoMPR)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HayatoMPR).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HayatoMPR).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HayatoMPR).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.HayatoStanceBonus)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HayatoStanceBonus).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HayatoStanceBonus).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HayatoStanceBonus).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.WillowDodge)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.WillowDodge).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.WillowDodge).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.WillowDodge).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.Shikigami)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Shikigami).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Shikigami).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Shikigami).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.HayatoPAD)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HayatoPAD).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HayatoPAD).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HayatoPAD).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.Jinsoku)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Jinsoku).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Jinsoku).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Jinsoku).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.HayatoCr)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HayatoCr).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HayatoCr).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HayatoCr).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.FireBarrier)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.FireBarrier).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FireBarrier).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FireBarrier).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ChangeFoxMan)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ChangeFoxMan).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ChangeFoxMan).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ChangeFoxMan).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.HakuBlessing)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HakuBlessing).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HakuBlessing).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HakuBlessing).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.HayatoBoss)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HayatoBoss).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HayatoBoss).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HayatoBoss).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.PLAYERS_BUFF430)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.PLAYERS_BUFF430).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PLAYERS_BUFF430).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PLAYERS_BUFF430).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.EyeForEye)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.EyeForEye).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EyeForEye).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.EyeForEye).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_439)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_439).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_439).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_439).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_155)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_155).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_155).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_155).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_524)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_524).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_524).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_524).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_526)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_526).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_526).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_526).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_527)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_527).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_527).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_527).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_528)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_528).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_528).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_528).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_529)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_529).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_529).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_529).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_530)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_530).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_530).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_530).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_531)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_531).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_531).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_531).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_532)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_532).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_532).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_532).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_533)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_533).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_533).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_533).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_534)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_534).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_534).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_534).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_538)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_538).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_538).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_538).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.FireBarrier)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.FireBarrier).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FireBarrier).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FireBarrier).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.ChangeFoxMan)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ChangeFoxMan).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ChangeFoxMan).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ChangeFoxMan).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.FixCoolTime)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.FixCoolTime).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FixCoolTime).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FixCoolTime).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IncMobRateDummy)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IncMobRateDummy).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncMobRateDummy).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IncMobRateDummy).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AdrenalinBoost)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AdrenalinBoost).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AdrenalinBoost).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AdrenalinBoost).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AranSmashSwing)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AranSmashSwing).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AranSmashSwing).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AranSmashSwing).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AranDrain)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AranDrain).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AranDrain).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AranDrain).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AranBoostEndHunt)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.AranBoostEndHunt).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AranBoostEndHunt).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AranBoostEndHunt).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.HiddenHyperLinkMaximization)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.HiddenHyperLinkMaximization).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HiddenHyperLinkMaximization).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HiddenHyperLinkMaximization).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RWCylinder)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RWCylinder).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWCylinder).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWCylinder).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RWCombination)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RWCombination).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWCombination).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWCombination).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_487)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_487).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_487).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_487).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RWMagnumBlow)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RWMagnumBlow).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWMagnumBlow).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWMagnumBlow).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RWBarrier)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RWBarrier).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWBarrier).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWBarrier).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RWBarrierHeal)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RWBarrierHeal).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWBarrierHeal).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWBarrierHeal).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RWMaximizeCannon)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RWMaximizeCannon).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWMaximizeCannon).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWMaximizeCannon).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RWOverHeat)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RWOverHeat).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWOverHeat).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWOverHeat).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RWMovingEvar)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RWMovingEvar).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWMovingEvar).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RWMovingEvar).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Stigma)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.Stigma).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Stigma).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Stigma).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_496)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_496).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_496).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_496).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_497)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_497).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_497).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_497).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_498)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_498).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_498).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_498).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_499)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_499).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_499).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_499).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_500)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_500).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_500).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_500).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_501)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_501).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_501).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_501).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.元氣覺醒)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.元氣覺醒).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.元氣覺醒).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.元氣覺醒).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.能量爆炸)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.能量爆炸).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.能量爆炸).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.能量爆炸).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_504)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_504).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_504).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_504).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.槍彈盛宴)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.槍彈盛宴).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.槍彈盛宴).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.槍彈盛宴).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_506)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_506).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_506).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_506).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.交換攻擊)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.交換攻擊).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.交換攻擊).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.交換攻擊).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.聖靈祈禱)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.聖靈祈禱).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.聖靈祈禱).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.聖靈祈禱).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_509)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_509).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_509).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_509).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_510)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_510).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_510).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_510).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_412)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_412).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_412).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_412).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_511)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_511).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_511).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_511).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.散式投擲)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.散式投擲).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.散式投擲).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.散式投擲).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_513)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_513).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_513).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_513).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_514)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_514).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_514).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_514).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.滅殺刃影)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.滅殺刃影).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.滅殺刃影).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.滅殺刃影).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_517)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_517).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_517).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_517).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.惡魔狂亂)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.惡魔狂亂).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.惡魔狂亂).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.惡魔狂亂).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_519)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_519).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_519).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_519).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_520)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_520).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_520).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_520).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_521)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_521).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_521).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_521).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_539)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_539).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_539).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_539).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_540)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_540).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_540).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_540).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_541)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_541).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_541).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_541).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_542)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_542).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_542).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_542).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_541)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_541).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_541).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_541).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_544)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_544).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_544).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_544).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_543)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_543).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_543).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_543).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_157)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_157).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_157).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_157).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_545)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_545).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_545).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_545).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_536)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_536).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_536).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_536).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_537)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_537).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_537).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_537).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.SoulMP)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulMP).xOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SoulMP).rOption);
        }
        if (hasNewStat(CharacterTemporaryStat.FullSoulMP)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.FullSoulMP).xOption);
        }

        short nBuffForSpecSize = 0;
        outPacket.encodeShort(nBuffForSpecSize);
        for (int i = 0; i < nBuffForSpecSize; i++) {
            outPacket.encodeInt(0); // nKey
            outPacket.encodeByte(0); // bEnable
        }

        if (hasNewStat(CharacterTemporaryStat.HayatoStance)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.HayatoStance).xOption);
        }

        outPacket.encodeByte(getDefenseAtt());
        outPacket.encodeByte(getDefenseState());
        outPacket.encodeByte(getPvpDamage());

        if (hasNewStat(CharacterTemporaryStat.Dice)) {
            for (int i = 0; i < getDiceInfo().length; i++) {
                outPacket.encodeInt(diceInfo[i]);
            }
        }
        if (hasNewStat(CharacterTemporaryStat.KeyDownMoving)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.KeyDownMoving).nOption);
        }
        if (hasNewStat(CharacterTemporaryStat.KillingPoint)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.KillingPoint).nOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PinkbeanRollingGrade)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.PinkbeanRollingGrade).nOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Judgement)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Judgement).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.StackBuff)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.StackBuff).mOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Trinity)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.Trinity).mOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ElementalCharge)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.ElementalCharge).mOption);
            outPacket.encodeShort(getOption(CharacterTemporaryStat.ElementalCharge).wOption);
            outPacket.encodeByte(getOption(CharacterTemporaryStat.ElementalCharge).uOption);
            outPacket.encodeByte(getOption(CharacterTemporaryStat.ElementalCharge).zOption);
        }
        if (hasNewStat(CharacterTemporaryStat.LifeTidal)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.LifeTidal).mOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AntiMagicShell)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.AntiMagicShell).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Larkness)) {
            getLarknessManager().getDarkInfo().encode(outPacket);
            getLarknessManager().getLightInfo().encode(outPacket);
            getLarknessManager().encode(outPacket);

        }
        if (hasNewStat(CharacterTemporaryStat.IgnoreTargetDEF)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IgnoreTargetDEF).mOption);
        }
        if (hasNewStat(CharacterTemporaryStat.StopForceAtomInfo)) {
            getStopForceAtom().encode(outPacket);
        }
        if (hasNewStat(CharacterTemporaryStat.SmashStack)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SmashStack).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MobZoneState)) {
            getMobZoneStates().forEach(outPacket::encodeInt);
            outPacket.encodeInt(0); // notify end
        }
        if (hasNewStat(CharacterTemporaryStat.Slow)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.Slow).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IceAura)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.IceAura).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.KnightsAura)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.KnightsAura).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IgnoreMobpdpR)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.IgnoreMobpdpR).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BdR)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.BdR).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DropRIncrease)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DropRIncrease).xOption);
            outPacket.encodeByte(getOption(CharacterTemporaryStat.DropRIncrease).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PoseType)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.PoseType).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Beholder)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Beholder).sOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Beholder).ssOption);
        }
        if (hasNewStat(CharacterTemporaryStat.CrossOverChain)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CrossOverChain).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Reincarnation)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Reincarnation).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ExtremeArchery)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ExtremeArchery).bOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ExtremeArchery).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.QuiverCatridge)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.QuiverCatridge).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ImmuneBarrier)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ImmuneBarrier).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ZeroAuraStr)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.ZeroAuraStr).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ZeroAuraSpd)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.ZeroAuraSpd).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ArmorPiercing)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ArmorPiercing).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SharpEyes)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SharpEyes).mOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AdvancedBless)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.AdvancedBless).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DebuffTolerance)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DebuffTolerance).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DotHealHPPerSecond)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DotHealHPPerSecond).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DotHealMPPerSecond)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DotHealMPPerSecond).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SpiritGuard)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SpiritGuard).nOption);
        }
        if (hasNewStat(CharacterTemporaryStat.KnockBack)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KnockBack).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KnockBack).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.ShieldAttack)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.ShieldAttack).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.SSFShootingAttack)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.SSFShootingAttack).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BMageAura)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BMageAura).xOption);
            outPacket.encodeByte(getOption(CharacterTemporaryStat.BMageAura).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.BattlePvPHelenaMark)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BattlePvPHelenaMark).cOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PinkbeanAttackBuff)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PinkbeanAttackBuff).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RoyalGuardState)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RoyalGuardState).bOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.RoyalGuardState).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.MichaelSoulLink)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MichaelSoulLink).xOption);
            outPacket.encodeByte(getOption(CharacterTemporaryStat.MichaelSoulLink).bOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MichaelSoulLink).cOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.MichaelSoulLink).yOption);
        }
        if (hasNewStat(CharacterTemporaryStat.AdrenalinBoost)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.AdrenalinBoost).cOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RWCylinder)) {
            outPacket.encodeByte(getOption(CharacterTemporaryStat.RWCylinder).bOption);
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RWCylinder).cOption);
        }
        if (hasNewStat(CharacterTemporaryStat.RWMagnumBlow)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.RWMagnumBlow).bOption);
            outPacket.encodeByte(getOption(CharacterTemporaryStat.RWMagnumBlow).xOption);
        }
        outPacket.encodeInt(getViperEnergyCharge());
        if (hasNewStat(CharacterTemporaryStat.BladeStance)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.BladeStance).xOption);
        }
        if (hasNewStat(CharacterTemporaryStat.DarkSight)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.DarkSight).cOption);
            outPacket.encodeInt(0);
        }
        if (hasNewStat(CharacterTemporaryStat.槍彈盛宴)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.槍彈盛宴).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_250)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_250).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.CriticalGrowing)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.CriticalGrowing).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.Ember)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.Ember).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.PickPocket)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.PickPocket).bOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_412)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_412).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_413)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_413).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_414)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_414).bOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_513)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_513).bOption);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }

        if (hasNewStat(CharacterTemporaryStat.VampDeath)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.VampDeath).bOption);
        }

        for (int i = 0; i < 9; i++) {
            if (hasNewStat(TSIndex.getCTSFromTwoStatIndex(i))) {
                getTwoStates().get(i).encode(outPacket);
            }
        }
        encodeCurrentAllIndieTempStat(outPacket);

        if (hasNewStat(CharacterTemporaryStat.UsingScouter)) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_544)) {
            outPacket.encodeInt(0);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_545)) {
            outPacket.encodeInt(0);
            outPacket.encodeByte(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_536)) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_537)) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_538)) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }
        //
    }

    private void encodeIndieTempStat(OutPacket outPacket, List<Option> options) {
        int curTime = (int) System.currentTimeMillis();
        outPacket.encodeInt(options.size());
        for (Option option : options) {
            outPacket.encodeInt(option.nReason);
            outPacket.encodeInt(option.nValue);
            outPacket.encodeInt(option.nKey);
            outPacket.encodeInt(curTime - option.tStart);
            outPacket.encodeInt(option.tTerm); // tTerm
            int idk = 0;
            outPacket.encodeInt(idk);
            for (int i = 0; i < idk; i++) {
                outPacket.encodeInt(0); // nMValueKey
                outPacket.encodeInt(0); // nMValue
            }
        }
    }

    private void encodeCurrentAllIndieTempStat(OutPacket outPacket) {
        List<CharacterTemporaryStat> stats = getCurrentStats().entrySet().stream()
                .filter(stat -> stat.getKey().isIndie())
                .map(Map.Entry::getKey).collect(Collectors.toList());

        for (CharacterTemporaryStat stat : stats) {
            encodeIndieTempStat(outPacket, getCurrentStats().get(stat));
        }
    }

    public void encodeRemovedIndieTempStat(OutPacket outPacket) {
        List<CharacterTemporaryStat> stats = getRemovedStats().entrySet().stream()
                .filter(stat -> stat.getKey().isIndie())
                .map(Map.Entry::getKey).collect(Collectors.toList());

        for (CharacterTemporaryStat stat : stats) {
            encodeIndieTempStat(outPacket, getRemovedStats().get(stat));
        }
    }


    public void encodeForRemote(OutPacket<GameClient> outPacket) {
        final int[] uFlagTemp = new int[CharacterTemporaryStat.SIZE];
        final ArrayList<Tuple<Integer, Integer>> uFlagData = new ArrayList<>();
        final ArrayList<CharacterTemporaryStat> aDefaultFlags = new ArrayList<>();

        aDefaultFlags.add(CharacterTemporaryStat.IndieStatCount);
        aDefaultFlags.add(CharacterTemporaryStat.PyramidEffect);
        aDefaultFlags.add(CharacterTemporaryStat.KillingPoint);
        aDefaultFlags.add(CharacterTemporaryStat.ZeroAuraStr);
        aDefaultFlags.add(CharacterTemporaryStat.ZeroAuraSpd);
        aDefaultFlags.add(CharacterTemporaryStat.BMageAura);
        aDefaultFlags.add(CharacterTemporaryStat.BattlePvPHelenaMark);
        aDefaultFlags.add(CharacterTemporaryStat.BattlePvPLangEProtection);
        aDefaultFlags.add(CharacterTemporaryStat.PinkbeanRollingGrade);
        aDefaultFlags.add(CharacterTemporaryStat.AdrenalinBoost);
        aDefaultFlags.add(CharacterTemporaryStat.RWBarrier);
        aDefaultFlags.add(CharacterTemporaryStat.IDA_BUFF_514);
        aDefaultFlags.add(CharacterTemporaryStat.EnergyCharged);
        aDefaultFlags.add(CharacterTemporaryStat.DashSpeed);
        aDefaultFlags.add(CharacterTemporaryStat.DashJump);
        aDefaultFlags.add(CharacterTemporaryStat.RideVehicle);
        aDefaultFlags.add(CharacterTemporaryStat.PartyBooster);
        aDefaultFlags.add(CharacterTemporaryStat.GuidedBullet);
        aDefaultFlags.add(CharacterTemporaryStat.Undead);
        aDefaultFlags.add(CharacterTemporaryStat.RideVehicleExpire);
        aDefaultFlags.add(CharacterTemporaryStat.COUNT_PLUS1);

        if (hasStat(CharacterTemporaryStat.Speed) || aDefaultFlags.contains(CharacterTemporaryStat.Speed)) {
            uFlagTemp[CharacterTemporaryStat.Speed.getPosition()] |= CharacterTemporaryStat.Speed.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Speed).nOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.ComboCounter) || aDefaultFlags.contains(CharacterTemporaryStat.ComboCounter)) {
            uFlagTemp[CharacterTemporaryStat.ComboCounter.getPosition()] |= CharacterTemporaryStat.ComboCounter.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ComboCounter).nOption, 1));
        }

        if (hasStat(CharacterTemporaryStat.IDA_BUFF_78) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_78)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_78.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_78.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_78).nOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_79) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_79)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_79.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_79.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_79).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_79).rOption, 4));
        }

        if (hasStat(CharacterTemporaryStat.WeaponCharge) || aDefaultFlags.contains(CharacterTemporaryStat.WeaponCharge)) {
            uFlagTemp[CharacterTemporaryStat.WeaponCharge.getPosition()] |= CharacterTemporaryStat.WeaponCharge.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.WeaponCharge).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.WeaponCharge).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.ElementalCharge) || aDefaultFlags.contains(CharacterTemporaryStat.ElementalCharge)) {
            uFlagTemp[CharacterTemporaryStat.ElementalCharge.getPosition()] |= CharacterTemporaryStat.ElementalCharge.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ElementalCharge).nOption, 2));
        }
        if (hasStat(CharacterTemporaryStat.Stun) || aDefaultFlags.contains(CharacterTemporaryStat.Stun)) {
            uFlagTemp[CharacterTemporaryStat.Stun.getPosition()] |= CharacterTemporaryStat.Stun.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Stun).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Stun).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Shock) || aDefaultFlags.contains(CharacterTemporaryStat.Shock)) {
            uFlagTemp[CharacterTemporaryStat.Shock.getPosition()] |= CharacterTemporaryStat.Shock.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Shock).nOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.Darkness) || aDefaultFlags.contains(CharacterTemporaryStat.Darkness)) {
            uFlagTemp[CharacterTemporaryStat.Darkness.getPosition()] |= CharacterTemporaryStat.Darkness.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Darkness).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Darkness).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Seal) || aDefaultFlags.contains(CharacterTemporaryStat.Seal)) {
            uFlagTemp[CharacterTemporaryStat.Seal.getPosition()] |= CharacterTemporaryStat.Seal.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Seal).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Seal).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Weakness) || aDefaultFlags.contains(CharacterTemporaryStat.Weakness)) {
            uFlagTemp[CharacterTemporaryStat.Weakness.getPosition()] |= CharacterTemporaryStat.Weakness.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Weakness).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Weakness).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.WeaknessMdamage) || aDefaultFlags.contains(CharacterTemporaryStat.WeaknessMdamage)) {
            uFlagTemp[CharacterTemporaryStat.WeaknessMdamage.getPosition()] |= CharacterTemporaryStat.WeaknessMdamage.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.WeaknessMdamage).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.WeaknessMdamage).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Curse) || aDefaultFlags.contains(CharacterTemporaryStat.Curse)) {
            uFlagTemp[CharacterTemporaryStat.Curse.getPosition()] |= CharacterTemporaryStat.Curse.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Curse).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Curse).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Slow) || aDefaultFlags.contains(CharacterTemporaryStat.Slow)) {
            uFlagTemp[CharacterTemporaryStat.Slow.getPosition()] |= CharacterTemporaryStat.Slow.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Slow).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Slow).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.PvPRaceEffect) || aDefaultFlags.contains(CharacterTemporaryStat.PvPRaceEffect)) {
            uFlagTemp[CharacterTemporaryStat.PvPRaceEffect.getPosition()] |= CharacterTemporaryStat.PvPRaceEffect.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.PvPRaceEffect).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.PvPRaceEffect).rOption, 4));
        }
//        if (hasStat(CharacterTemporaryStat.IceKnight)|| aDefaultFlags.contains(CharacterTemporaryStat.IceKnight)) {
//            uFlagTemp[CharacterTemporaryStat.IceKnight.getPosition()] |= CharacterTemporaryStat.IceKnight.getValue();
//            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IceKnight).nOption, 2));
//            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IceKnight).rOption, 4));
//        }
        if (hasStat(CharacterTemporaryStat.TimeBomb) || aDefaultFlags.contains(CharacterTemporaryStat.TimeBomb)) {
            uFlagTemp[CharacterTemporaryStat.TimeBomb.getPosition()] |= CharacterTemporaryStat.TimeBomb.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.TimeBomb).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.TimeBomb).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Team) || aDefaultFlags.contains(CharacterTemporaryStat.Team)) {
            uFlagTemp[CharacterTemporaryStat.Team.getPosition()] |= CharacterTemporaryStat.Team.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Team).nOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.Disorder) || aDefaultFlags.contains(CharacterTemporaryStat.Disorder)) {
            uFlagTemp[CharacterTemporaryStat.Disorder.getPosition()] |= CharacterTemporaryStat.Disorder.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Disorder).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Disorder).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Thread) || aDefaultFlags.contains(CharacterTemporaryStat.Thread)) {
            uFlagTemp[CharacterTemporaryStat.Thread.getPosition()] |= CharacterTemporaryStat.Thread.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Thread).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Thread).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Poison) || aDefaultFlags.contains(CharacterTemporaryStat.Poison)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Poison).nOption, 2));
        }
        if (hasStat(CharacterTemporaryStat.Poison) || aDefaultFlags.contains(CharacterTemporaryStat.Poison)) {
            uFlagTemp[CharacterTemporaryStat.Poison.getPosition()] |= CharacterTemporaryStat.Poison.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Poison).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Poison).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.ShadowPartner) || aDefaultFlags.contains(CharacterTemporaryStat.ShadowPartner)) {
            uFlagTemp[CharacterTemporaryStat.ShadowPartner.getPosition()] |= CharacterTemporaryStat.ShadowPartner.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ShadowPartner).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ShadowPartner).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.DarkSight) || aDefaultFlags.contains(CharacterTemporaryStat.DarkSight)) {
            uFlagTemp[CharacterTemporaryStat.DarkSight.getPosition()] |= CharacterTemporaryStat.DarkSight.getValue();
        }
        if (hasStat(CharacterTemporaryStat.SoulArrow) || aDefaultFlags.contains(CharacterTemporaryStat.SoulArrow)) {
            uFlagTemp[CharacterTemporaryStat.SoulArrow.getPosition()] |= CharacterTemporaryStat.SoulArrow.getValue();
        }
        if (hasStat(CharacterTemporaryStat.Morph) || aDefaultFlags.contains(CharacterTemporaryStat.Morph)) {
            uFlagTemp[CharacterTemporaryStat.Morph.getPosition()] |= CharacterTemporaryStat.Morph.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Morph).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Morph).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Ghost) || aDefaultFlags.contains(CharacterTemporaryStat.Ghost)) {
            uFlagTemp[CharacterTemporaryStat.Ghost.getPosition()] |= CharacterTemporaryStat.Ghost.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Ghost).nOption, 2));
        }
        if (hasStat(CharacterTemporaryStat.Attract) || aDefaultFlags.contains(CharacterTemporaryStat.Attract)) {
            uFlagTemp[CharacterTemporaryStat.Attract.getPosition()] |= CharacterTemporaryStat.Attract.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Attract).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Attract).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Magnet) || aDefaultFlags.contains(CharacterTemporaryStat.Magnet)) {
            uFlagTemp[CharacterTemporaryStat.Magnet.getPosition()] |= CharacterTemporaryStat.Magnet.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Magnet).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Magnet).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.MagnetArea) || aDefaultFlags.contains(CharacterTemporaryStat.MagnetArea)) {
            uFlagTemp[CharacterTemporaryStat.MagnetArea.getPosition()] |= CharacterTemporaryStat.MagnetArea.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.MagnetArea).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.MagnetArea).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.NoBulletConsume) || aDefaultFlags.contains(CharacterTemporaryStat.NoBulletConsume)) {
            uFlagTemp[CharacterTemporaryStat.NoBulletConsume.getPosition()] |= CharacterTemporaryStat.NoBulletConsume.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.NoBulletConsume).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.BanMap) || aDefaultFlags.contains(CharacterTemporaryStat.BanMap)) {
            uFlagTemp[CharacterTemporaryStat.BanMap.getPosition()] |= CharacterTemporaryStat.BanMap.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BanMap).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BanMap).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Barrier) || aDefaultFlags.contains(CharacterTemporaryStat.Barrier)) {
            uFlagTemp[CharacterTemporaryStat.Barrier.getPosition()] |= CharacterTemporaryStat.Barrier.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Barrier).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Barrier).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.DojangShield) || aDefaultFlags.contains(CharacterTemporaryStat.DojangShield)) {
            uFlagTemp[CharacterTemporaryStat.DojangShield.getPosition()] |= CharacterTemporaryStat.DojangShield.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DojangShield).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DojangShield).rOption, 4));
        }

        if (hasStat(CharacterTemporaryStat.ReverseInput) || aDefaultFlags.contains(CharacterTemporaryStat.ReverseInput)) {
            uFlagTemp[CharacterTemporaryStat.ReverseInput.getPosition()] |= CharacterTemporaryStat.ReverseInput.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ReverseInput).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ReverseInput).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.RespectPImmune) || aDefaultFlags.contains(CharacterTemporaryStat.RespectPImmune)) {
            uFlagTemp[CharacterTemporaryStat.RespectPImmune.getPosition()] |= CharacterTemporaryStat.RespectPImmune.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.RespectPImmune).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.RespectMImmune) || aDefaultFlags.contains(CharacterTemporaryStat.RespectMImmune)) {
            uFlagTemp[CharacterTemporaryStat.RespectMImmune.getPosition()] |= CharacterTemporaryStat.RespectMImmune.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.RespectMImmune).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.DefenseAtt) || aDefaultFlags.contains(CharacterTemporaryStat.DefenseAtt)) {
            uFlagTemp[CharacterTemporaryStat.DefenseAtt.getPosition()] |= CharacterTemporaryStat.DefenseAtt.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DefenseAtt).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.DefenseState) || aDefaultFlags.contains(CharacterTemporaryStat.DefenseState)) {
            uFlagTemp[CharacterTemporaryStat.DefenseState.getPosition()] |= CharacterTemporaryStat.DefenseState.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DefenseState).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.DojangBerserk) || aDefaultFlags.contains(CharacterTemporaryStat.DojangBerserk)) {
            uFlagTemp[CharacterTemporaryStat.DojangBerserk.getPosition()] |= CharacterTemporaryStat.DojangBerserk.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DojangBerserk).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DojangBerserk).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.DojangInvincible) || aDefaultFlags.contains(CharacterTemporaryStat.DojangInvincible)) {
            uFlagTemp[CharacterTemporaryStat.DojangInvincible.getPosition()] |= CharacterTemporaryStat.DojangInvincible.getValue();
        }
        if (hasStat(CharacterTemporaryStat.RepeatEffect) || aDefaultFlags.contains(CharacterTemporaryStat.RepeatEffect)) {
            uFlagTemp[CharacterTemporaryStat.RepeatEffect.getPosition()] |= CharacterTemporaryStat.RepeatEffect.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.RepeatEffect).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.RepeatEffect).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.ExpBuffRate) || aDefaultFlags.contains(CharacterTemporaryStat.ExpBuffRate)) {
            uFlagTemp[CharacterTemporaryStat.ExpBuffRate.getPosition()] |= CharacterTemporaryStat.ExpBuffRate.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ExpBuffRate).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ExpBuffRate).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.PLAYERS_BUFF430) || aDefaultFlags.contains(CharacterTemporaryStat.PLAYERS_BUFF430)) {
            uFlagTemp[CharacterTemporaryStat.PLAYERS_BUFF430.getPosition()] |= CharacterTemporaryStat.PLAYERS_BUFF430.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.PLAYERS_BUFF430).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.PLAYERS_BUFF430).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.StopPortion) || aDefaultFlags.contains(CharacterTemporaryStat.StopPortion)) {
            uFlagTemp[CharacterTemporaryStat.StopPortion.getPosition()] |= CharacterTemporaryStat.StopPortion.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.StopPortion).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.StopPortion).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Fear) || aDefaultFlags.contains(CharacterTemporaryStat.Fear)) {
            uFlagTemp[CharacterTemporaryStat.Fear.getPosition()] |= CharacterTemporaryStat.Fear.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Fear).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Fear).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_133) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_133)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_133.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_133.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_133).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_133).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.MagicShield) || aDefaultFlags.contains(CharacterTemporaryStat.MagicShield)) {
            uFlagTemp[CharacterTemporaryStat.MagicShield.getPosition()] |= CharacterTemporaryStat.MagicShield.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.MagicShield).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Flying) || aDefaultFlags.contains(CharacterTemporaryStat.Flying)) {
            uFlagTemp[CharacterTemporaryStat.Flying.getPosition()] |= CharacterTemporaryStat.Flying.getValue();
        }
        if (hasStat(CharacterTemporaryStat.Frozen) || aDefaultFlags.contains(CharacterTemporaryStat.Frozen)) {
            uFlagTemp[CharacterTemporaryStat.Frozen.getPosition()] |= CharacterTemporaryStat.Frozen.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Frozen).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Frozen).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Frozen2) || aDefaultFlags.contains(CharacterTemporaryStat.Frozen2)) {
            uFlagTemp[CharacterTemporaryStat.Frozen2.getPosition()] |= CharacterTemporaryStat.Frozen2.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Frozen2).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Frozen2).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Web) || aDefaultFlags.contains(CharacterTemporaryStat.Web)) {
            uFlagTemp[CharacterTemporaryStat.Web.getPosition()] |= CharacterTemporaryStat.Web.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Web).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Web).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.DrawBack) || aDefaultFlags.contains(CharacterTemporaryStat.DrawBack)) {
            uFlagTemp[CharacterTemporaryStat.DrawBack.getPosition()] |= CharacterTemporaryStat.DrawBack.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DrawBack).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DrawBack).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.FinalCut) || aDefaultFlags.contains(CharacterTemporaryStat.FinalCut)) {
            uFlagTemp[CharacterTemporaryStat.FinalCut.getPosition()] |= CharacterTemporaryStat.FinalCut.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FinalCut).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FinalCut).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Cyclone) || aDefaultFlags.contains(CharacterTemporaryStat.Cyclone)) {
            uFlagTemp[CharacterTemporaryStat.Cyclone.getPosition()] |= CharacterTemporaryStat.Cyclone.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Cyclone).nOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.OnCapsule) || aDefaultFlags.contains(CharacterTemporaryStat.OnCapsule)) {
            uFlagTemp[CharacterTemporaryStat.OnCapsule.getPosition()] |= CharacterTemporaryStat.OnCapsule.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.OnCapsule).nOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.Sneak) || aDefaultFlags.contains(CharacterTemporaryStat.Sneak)) {
            uFlagTemp[CharacterTemporaryStat.Sneak.getPosition()] |= CharacterTemporaryStat.Sneak.getValue();
        }
        if (hasStat(CharacterTemporaryStat.BeastFormDamageUp) || aDefaultFlags.contains(CharacterTemporaryStat.BeastFormDamageUp)) {
            uFlagTemp[CharacterTemporaryStat.BeastFormDamageUp.getPosition()] |= CharacterTemporaryStat.BeastFormDamageUp.getValue();
        }
        if (hasStat(CharacterTemporaryStat.Mechanic) || aDefaultFlags.contains(CharacterTemporaryStat.Mechanic)) {
            uFlagTemp[CharacterTemporaryStat.Mechanic.getPosition()] |= CharacterTemporaryStat.Mechanic.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Mechanic).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Mechanic).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.BlessingArmor) || aDefaultFlags.contains(CharacterTemporaryStat.BlessingArmor)) {
            uFlagTemp[CharacterTemporaryStat.BlessingArmor.getPosition()] |= CharacterTemporaryStat.BlessingArmor.getValue();
        }
        if (hasStat(CharacterTemporaryStat.BlessingArmorIncPAD) || aDefaultFlags.contains(CharacterTemporaryStat.BlessingArmorIncPAD)) {
            uFlagTemp[CharacterTemporaryStat.BlessingArmorIncPAD.getPosition()] |= CharacterTemporaryStat.BlessingArmorIncPAD.getValue();
        }
        if (hasStat(CharacterTemporaryStat.Inflation) || aDefaultFlags.contains(CharacterTemporaryStat.Inflation)) {
            uFlagTemp[CharacterTemporaryStat.Inflation.getPosition()] |= CharacterTemporaryStat.Inflation.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Inflation).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Inflation).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Explosion) || aDefaultFlags.contains(CharacterTemporaryStat.Explosion)) {
            uFlagTemp[CharacterTemporaryStat.Explosion.getPosition()] |= CharacterTemporaryStat.Explosion.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Explosion).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Explosion).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.DarkTornado) || aDefaultFlags.contains(CharacterTemporaryStat.DarkTornado)) {
            uFlagTemp[CharacterTemporaryStat.DarkTornado.getPosition()] |= CharacterTemporaryStat.DarkTornado.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DarkTornado).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DarkTornado).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.AmplifyDamage) || aDefaultFlags.contains(CharacterTemporaryStat.AmplifyDamage)) {
            uFlagTemp[CharacterTemporaryStat.AmplifyDamage.getPosition()] |= CharacterTemporaryStat.AmplifyDamage.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AmplifyDamage).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AmplifyDamage).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.HideAttack) || aDefaultFlags.contains(CharacterTemporaryStat.HideAttack)) {
            uFlagTemp[CharacterTemporaryStat.HideAttack.getPosition()] |= CharacterTemporaryStat.HideAttack.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HideAttack).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HideAttack).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.HolyMagicShell) || aDefaultFlags.contains(CharacterTemporaryStat.HolyMagicShell)) {
            uFlagTemp[CharacterTemporaryStat.HolyMagicShell.getPosition()] |= CharacterTemporaryStat.HolyMagicShell.getValue();
        }
        if (hasStat(CharacterTemporaryStat.DevilishPower) || aDefaultFlags.contains(CharacterTemporaryStat.DevilishPower)) {
            uFlagTemp[CharacterTemporaryStat.DevilishPower.getPosition()] |= CharacterTemporaryStat.DevilishPower.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DevilishPower).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DevilishPower).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.SpiritLink) || aDefaultFlags.contains(CharacterTemporaryStat.SpiritLink)) {
            uFlagTemp[CharacterTemporaryStat.SpiritLink.getPosition()] |= CharacterTemporaryStat.SpiritLink.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.SpiritLink).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.SpiritLink).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Event) || aDefaultFlags.contains(CharacterTemporaryStat.Event)) {
            uFlagTemp[CharacterTemporaryStat.Event.getPosition()] |= CharacterTemporaryStat.Event.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Event).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Event).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Event2) || aDefaultFlags.contains(CharacterTemporaryStat.Event2)) {
            uFlagTemp[CharacterTemporaryStat.Event2.getPosition()] |= CharacterTemporaryStat.Event2.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Event2).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Event2).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.DeathMark) || aDefaultFlags.contains(CharacterTemporaryStat.DeathMark)) {
            uFlagTemp[CharacterTemporaryStat.DeathMark.getPosition()] |= CharacterTemporaryStat.DeathMark.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DeathMark).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DeathMark).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.PainMark) || aDefaultFlags.contains(CharacterTemporaryStat.PainMark)) {
            uFlagTemp[CharacterTemporaryStat.PainMark.getPosition()] |= CharacterTemporaryStat.PainMark.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.PainMark).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.PainMark).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Lapidification) || aDefaultFlags.contains(CharacterTemporaryStat.Lapidification)) {
            uFlagTemp[CharacterTemporaryStat.Lapidification.getPosition()] |= CharacterTemporaryStat.Lapidification.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Lapidification).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Lapidification).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.VampDeath) || aDefaultFlags.contains(CharacterTemporaryStat.VampDeath)) {
            uFlagTemp[CharacterTemporaryStat.VampDeath.getPosition()] |= CharacterTemporaryStat.VampDeath.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.VampDeath).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.VampDeath).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.VampDeathSummon) || aDefaultFlags.contains(CharacterTemporaryStat.VampDeathSummon)) {
            uFlagTemp[CharacterTemporaryStat.VampDeathSummon.getPosition()] |= CharacterTemporaryStat.VampDeathSummon.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.VampDeathSummon).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.VampDeathSummon).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.VenomSnake) || aDefaultFlags.contains(CharacterTemporaryStat.VenomSnake)) {
            uFlagTemp[CharacterTemporaryStat.VenomSnake.getPosition()] |= CharacterTemporaryStat.VenomSnake.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.VenomSnake).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.VenomSnake).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.PyramidEffect) || aDefaultFlags.contains(CharacterTemporaryStat.PyramidEffect)) {
            uFlagTemp[CharacterTemporaryStat.PyramidEffect.getPosition()] |= CharacterTemporaryStat.PyramidEffect.getValue();
            uFlagData.add(new Tuple<>(-1 /*getOption(CharacterTemporaryStat.PyramidEffect).nOption*/, 4));
        }
        if (hasStat(CharacterTemporaryStat.KillingPoint) || aDefaultFlags.contains(CharacterTemporaryStat.KillingPoint)) {
            uFlagTemp[CharacterTemporaryStat.KillingPoint.getPosition()] |= CharacterTemporaryStat.KillingPoint.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.KillingPoint).nOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.PinkbeanRollingGrade) || aDefaultFlags.contains(CharacterTemporaryStat.PinkbeanRollingGrade)) {
            uFlagTemp[CharacterTemporaryStat.PinkbeanRollingGrade.getPosition()] |= CharacterTemporaryStat.PinkbeanRollingGrade.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.PinkbeanRollingGrade).nOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.IgnoreTargetDEF) || aDefaultFlags.contains(CharacterTemporaryStat.IgnoreTargetDEF)) {
            uFlagTemp[CharacterTemporaryStat.IgnoreTargetDEF.getPosition()] |= CharacterTemporaryStat.IgnoreTargetDEF.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IgnoreTargetDEF).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IgnoreTargetDEF).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Invisible) || aDefaultFlags.contains(CharacterTemporaryStat.Invisible)) {
            uFlagTemp[CharacterTemporaryStat.Invisible.getPosition()] |= CharacterTemporaryStat.Invisible.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Invisible).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Invisible).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Judgement) || aDefaultFlags.contains(CharacterTemporaryStat.Judgement)) {
            uFlagTemp[CharacterTemporaryStat.Judgement.getPosition()] |= CharacterTemporaryStat.Judgement.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Judgement).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Judgement).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.KeyDownAreaMoving) || aDefaultFlags.contains(CharacterTemporaryStat.KeyDownAreaMoving)) {
            uFlagTemp[CharacterTemporaryStat.KeyDownAreaMoving.getPosition()] |= CharacterTemporaryStat.KeyDownAreaMoving.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.KeyDownAreaMoving).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.KeyDownAreaMoving).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.StackBuff) || aDefaultFlags.contains(CharacterTemporaryStat.StackBuff)) {
            uFlagTemp[CharacterTemporaryStat.StackBuff.getPosition()] |= CharacterTemporaryStat.StackBuff.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.StackBuff).nOption, 2));
        }
        if (hasStat(CharacterTemporaryStat.BlessOfDarkness) || aDefaultFlags.contains(CharacterTemporaryStat.BlessOfDarkness)) {
            uFlagTemp[CharacterTemporaryStat.BlessOfDarkness.getPosition()] |= CharacterTemporaryStat.BlessOfDarkness.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BlessOfDarkness).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Larkness) || aDefaultFlags.contains(CharacterTemporaryStat.Larkness)) {
            uFlagTemp[CharacterTemporaryStat.Larkness.getPosition()] |= CharacterTemporaryStat.Larkness.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Larkness).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Larkness).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.ReshuffleSwitch) || aDefaultFlags.contains(CharacterTemporaryStat.ReshuffleSwitch)) {
            uFlagTemp[CharacterTemporaryStat.ReshuffleSwitch.getPosition()] |= CharacterTemporaryStat.ReshuffleSwitch.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ReshuffleSwitch).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ReshuffleSwitch).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.SpecialAction) || aDefaultFlags.contains(CharacterTemporaryStat.SpecialAction)) {
            uFlagTemp[CharacterTemporaryStat.SpecialAction.getPosition()] |= CharacterTemporaryStat.SpecialAction.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.SpecialAction).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.SpecialAction).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.StopForceAtomInfo) || aDefaultFlags.contains(CharacterTemporaryStat.StopForceAtomInfo)) {
            uFlagTemp[CharacterTemporaryStat.StopForceAtomInfo.getPosition()] |= CharacterTemporaryStat.StopForceAtomInfo.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.StopForceAtomInfo).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.StopForceAtomInfo).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.SoulGazeCriDamR) || aDefaultFlags.contains(CharacterTemporaryStat.SoulGazeCriDamR)) {
            uFlagTemp[CharacterTemporaryStat.SoulGazeCriDamR.getPosition()] |= CharacterTemporaryStat.SoulGazeCriDamR.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.SoulGazeCriDamR).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.SoulGazeCriDamR).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.PowerTransferGauge) || aDefaultFlags.contains(CharacterTemporaryStat.PowerTransferGauge)) {
            uFlagTemp[CharacterTemporaryStat.PowerTransferGauge.getPosition()] |= CharacterTemporaryStat.PowerTransferGauge.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.PowerTransferGauge).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.PowerTransferGauge).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_539) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_539)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_539.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_539.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_539).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_539).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.AffinitySlug) || aDefaultFlags.contains(CharacterTemporaryStat.AffinitySlug)) {
            uFlagTemp[CharacterTemporaryStat.AffinitySlug.getPosition()] |= CharacterTemporaryStat.AffinitySlug.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AffinitySlug).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AffinitySlug).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.SoulExalt) || aDefaultFlags.contains(CharacterTemporaryStat.SoulExalt)) {
            uFlagTemp[CharacterTemporaryStat.SoulExalt.getPosition()] |= CharacterTemporaryStat.SoulExalt.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.SoulExalt).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.SoulExalt).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.HiddenPieceOn) || aDefaultFlags.contains(CharacterTemporaryStat.HiddenPieceOn)) {
            uFlagTemp[CharacterTemporaryStat.HiddenPieceOn.getPosition()] |= CharacterTemporaryStat.HiddenPieceOn.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HiddenPieceOn).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HiddenPieceOn).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.SmashStack) || aDefaultFlags.contains(CharacterTemporaryStat.SmashStack)) {
            uFlagTemp[CharacterTemporaryStat.SmashStack.getPosition()] |= CharacterTemporaryStat.SmashStack.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.SmashStack).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.SmashStack).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.MobZoneState) || aDefaultFlags.contains(CharacterTemporaryStat.MobZoneState)) {
            uFlagTemp[CharacterTemporaryStat.MobZoneState.getPosition()] |= CharacterTemporaryStat.MobZoneState.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.MobZoneState).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.MobZoneState).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.GiveMeHeal) || aDefaultFlags.contains(CharacterTemporaryStat.GiveMeHeal)) {
            uFlagTemp[CharacterTemporaryStat.GiveMeHeal.getPosition()] |= CharacterTemporaryStat.GiveMeHeal.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.GiveMeHeal).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.GiveMeHeal).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.TouchMe) || aDefaultFlags.contains(CharacterTemporaryStat.TouchMe)) {
            uFlagTemp[CharacterTemporaryStat.TouchMe.getPosition()] |= CharacterTemporaryStat.TouchMe.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.TouchMe).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.TouchMe).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Contagion) || aDefaultFlags.contains(CharacterTemporaryStat.Contagion)) {
            uFlagTemp[CharacterTemporaryStat.Contagion.getPosition()] |= CharacterTemporaryStat.Contagion.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Contagion).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Contagion).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Contagion) || aDefaultFlags.contains(CharacterTemporaryStat.Contagion)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Contagion).tOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.ComboUnlimited) || aDefaultFlags.contains(CharacterTemporaryStat.ComboUnlimited)) {
            uFlagTemp[CharacterTemporaryStat.ComboUnlimited.getPosition()] |= CharacterTemporaryStat.ComboUnlimited.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ComboUnlimited).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ComboUnlimited).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IgnorePCounter) || aDefaultFlags.contains(CharacterTemporaryStat.IgnorePCounter)) {
            uFlagTemp[CharacterTemporaryStat.IgnorePCounter.getPosition()] |= CharacterTemporaryStat.IgnorePCounter.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IgnorePCounter).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IgnorePCounter).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IgnoreAllCounter) || aDefaultFlags.contains(CharacterTemporaryStat.IgnoreAllCounter)) {
            uFlagTemp[CharacterTemporaryStat.IgnoreAllCounter.getPosition()] |= CharacterTemporaryStat.IgnoreAllCounter.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IgnoreAllCounter).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IgnoreAllCounter).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IgnorePImmune) || aDefaultFlags.contains(CharacterTemporaryStat.IgnorePImmune)) {
            uFlagTemp[CharacterTemporaryStat.IgnorePImmune.getPosition()] |= CharacterTemporaryStat.IgnorePImmune.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IgnorePImmune).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IgnorePImmune).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IgnoreAllImmune) || aDefaultFlags.contains(CharacterTemporaryStat.IgnoreAllImmune)) {
            uFlagTemp[CharacterTemporaryStat.IgnoreAllImmune.getPosition()] |= CharacterTemporaryStat.IgnoreAllImmune.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IgnoreAllImmune).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IgnoreAllImmune).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.FinalJudgement) || aDefaultFlags.contains(CharacterTemporaryStat.FinalJudgement)) {
            uFlagTemp[CharacterTemporaryStat.FinalJudgement.getPosition()] |= CharacterTemporaryStat.FinalJudgement.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FinalJudgement).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FinalJudgement).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_284) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_284)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_284.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_284.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_284).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_284).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.KnightsAura) || aDefaultFlags.contains(CharacterTemporaryStat.KnightsAura)) {
            uFlagTemp[CharacterTemporaryStat.KnightsAura.getPosition()] |= CharacterTemporaryStat.KnightsAura.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.KnightsAura).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.KnightsAura).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IceAura) || aDefaultFlags.contains(CharacterTemporaryStat.IceAura)) {
            uFlagTemp[CharacterTemporaryStat.IceAura.getPosition()] |= CharacterTemporaryStat.IceAura.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IceAura).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IceAura).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.FireAura) || aDefaultFlags.contains(CharacterTemporaryStat.FireAura)) {
            uFlagTemp[CharacterTemporaryStat.FireAura.getPosition()] |= CharacterTemporaryStat.FireAura.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FireAura).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FireAura).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.VengeanceOfAngel) || aDefaultFlags.contains(CharacterTemporaryStat.VengeanceOfAngel)) {
            uFlagTemp[CharacterTemporaryStat.VengeanceOfAngel.getPosition()] |= CharacterTemporaryStat.VengeanceOfAngel.getValue();
        }
        if (hasStat(CharacterTemporaryStat.HeavensDoor) || aDefaultFlags.contains(CharacterTemporaryStat.HeavensDoor)) {
            uFlagTemp[CharacterTemporaryStat.HeavensDoor.getPosition()] |= CharacterTemporaryStat.HeavensDoor.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HeavensDoor).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HeavensDoor).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.DamAbsorbShield) || aDefaultFlags.contains(CharacterTemporaryStat.DamAbsorbShield)) {
            uFlagTemp[CharacterTemporaryStat.DamAbsorbShield.getPosition()] |= CharacterTemporaryStat.DamAbsorbShield.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DamAbsorbShield).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DamAbsorbShield).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.AntiMagicShell) || aDefaultFlags.contains(CharacterTemporaryStat.AntiMagicShell)) {
            uFlagTemp[CharacterTemporaryStat.AntiMagicShell.getPosition()] |= CharacterTemporaryStat.AntiMagicShell.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AntiMagicShell).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AntiMagicShell).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.NotDamaged) || aDefaultFlags.contains(CharacterTemporaryStat.NotDamaged)) {
            uFlagTemp[CharacterTemporaryStat.NotDamaged.getPosition()] |= CharacterTemporaryStat.NotDamaged.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.NotDamaged).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.NotDamaged).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.BleedingToxin) || aDefaultFlags.contains(CharacterTemporaryStat.BleedingToxin)) {
            uFlagTemp[CharacterTemporaryStat.BleedingToxin.getPosition()] |= CharacterTemporaryStat.BleedingToxin.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BleedingToxin).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BleedingToxin).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.WindBreakerFinal) || aDefaultFlags.contains(CharacterTemporaryStat.WindBreakerFinal)) {
            uFlagTemp[CharacterTemporaryStat.WindBreakerFinal.getPosition()] |= CharacterTemporaryStat.WindBreakerFinal.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.WindBreakerFinal).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.WindBreakerFinal).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IgnoreMobDamR) || aDefaultFlags.contains(CharacterTemporaryStat.IgnoreMobDamR)) {
            uFlagTemp[CharacterTemporaryStat.IgnoreMobDamR.getPosition()] |= CharacterTemporaryStat.IgnoreMobDamR.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IgnoreMobDamR).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IgnoreMobDamR).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Asura) || aDefaultFlags.contains(CharacterTemporaryStat.Asura)) {
            uFlagTemp[CharacterTemporaryStat.Asura.getPosition()] |= CharacterTemporaryStat.Asura.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Asura).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Asura).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_296) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_296)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_296.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_296.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_296).rOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_296).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.UnityOfPower) || aDefaultFlags.contains(CharacterTemporaryStat.UnityOfPower)) {
            uFlagTemp[CharacterTemporaryStat.UnityOfPower.getPosition()] |= CharacterTemporaryStat.UnityOfPower.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.UnityOfPower).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.UnityOfPower).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Stimulate) || aDefaultFlags.contains(CharacterTemporaryStat.Stimulate)) {
            uFlagTemp[CharacterTemporaryStat.Stimulate.getPosition()] |= CharacterTemporaryStat.Stimulate.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Stimulate).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Stimulate).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.ReturnTeleport) || aDefaultFlags.contains(CharacterTemporaryStat.ReturnTeleport)) {
            uFlagTemp[CharacterTemporaryStat.ReturnTeleport.getPosition()] |= CharacterTemporaryStat.ReturnTeleport.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ReturnTeleport).nOption, 1));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ReturnTeleport).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.CapDebuff) || aDefaultFlags.contains(CharacterTemporaryStat.CapDebuff)) {
            uFlagTemp[CharacterTemporaryStat.CapDebuff.getPosition()] |= CharacterTemporaryStat.CapDebuff.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.CapDebuff).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.CapDebuff).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.OverloadCount) || aDefaultFlags.contains(CharacterTemporaryStat.OverloadCount)) {
            uFlagTemp[CharacterTemporaryStat.OverloadCount.getPosition()] |= CharacterTemporaryStat.OverloadCount.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.OverloadCount).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.OverloadCount).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.FireBomb) || aDefaultFlags.contains(CharacterTemporaryStat.FireBomb)) {
            uFlagTemp[CharacterTemporaryStat.FireBomb.getPosition()] |= CharacterTemporaryStat.FireBomb.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FireBomb).nOption, 1));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FireBomb).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.SurplusSupply) || aDefaultFlags.contains(CharacterTemporaryStat.SurplusSupply)) {
            uFlagTemp[CharacterTemporaryStat.SurplusSupply.getPosition()] |= CharacterTemporaryStat.SurplusSupply.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.SurplusSupply).nOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.NewFlying) || aDefaultFlags.contains(CharacterTemporaryStat.NewFlying)) {
            uFlagTemp[CharacterTemporaryStat.NewFlying.getPosition()] |= CharacterTemporaryStat.NewFlying.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.NewFlying).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.NewFlying).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.NaviFlying) || aDefaultFlags.contains(CharacterTemporaryStat.NaviFlying)) {
            uFlagTemp[CharacterTemporaryStat.NaviFlying.getPosition()] |= CharacterTemporaryStat.NaviFlying.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.NaviFlying).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.NaviFlying).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.AmaranthGenerator) || aDefaultFlags.contains(CharacterTemporaryStat.AmaranthGenerator)) {
            uFlagTemp[CharacterTemporaryStat.AmaranthGenerator.getPosition()] |= CharacterTemporaryStat.AmaranthGenerator.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AmaranthGenerator).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AmaranthGenerator).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.CygnusElementSkill) || aDefaultFlags.contains(CharacterTemporaryStat.CygnusElementSkill)) {
            uFlagTemp[CharacterTemporaryStat.CygnusElementSkill.getPosition()] |= CharacterTemporaryStat.CygnusElementSkill.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.CygnusElementSkill).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.CygnusElementSkill).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.StrikerHyperElectric) || aDefaultFlags.contains(CharacterTemporaryStat.StrikerHyperElectric)) {
            uFlagTemp[CharacterTemporaryStat.StrikerHyperElectric.getPosition()] |= CharacterTemporaryStat.StrikerHyperElectric.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.StrikerHyperElectric).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.StrikerHyperElectric).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.EventPointAbsorb) || aDefaultFlags.contains(CharacterTemporaryStat.EventPointAbsorb)) {
            uFlagTemp[CharacterTemporaryStat.EventPointAbsorb.getPosition()] |= CharacterTemporaryStat.EventPointAbsorb.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.EventPointAbsorb).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.EventPointAbsorb).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.EventAssemble) || aDefaultFlags.contains(CharacterTemporaryStat.EventAssemble)) {
            uFlagTemp[CharacterTemporaryStat.EventAssemble.getPosition()] |= CharacterTemporaryStat.EventAssemble.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.EventAssemble).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.EventAssemble).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Albatross) || aDefaultFlags.contains(CharacterTemporaryStat.Albatross)) {
            uFlagTemp[CharacterTemporaryStat.Albatross.getPosition()] |= CharacterTemporaryStat.Albatross.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Albatross).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Albatross).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Translucence) || aDefaultFlags.contains(CharacterTemporaryStat.Translucence)) {
            uFlagTemp[CharacterTemporaryStat.Translucence.getPosition()] |= CharacterTemporaryStat.Translucence.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Translucence).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Translucence).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.PoseType) || aDefaultFlags.contains(CharacterTemporaryStat.PoseType)) {
            uFlagTemp[CharacterTemporaryStat.PoseType.getPosition()] |= CharacterTemporaryStat.PoseType.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.PoseType).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.PoseType).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.LightOfSpirit) || aDefaultFlags.contains(CharacterTemporaryStat.LightOfSpirit)) {
            uFlagTemp[CharacterTemporaryStat.LightOfSpirit.getPosition()] |= CharacterTemporaryStat.LightOfSpirit.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.LightOfSpirit).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.LightOfSpirit).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.ElementSoul) || aDefaultFlags.contains(CharacterTemporaryStat.ElementSoul)) {
            uFlagTemp[CharacterTemporaryStat.ElementSoul.getPosition()] |= CharacterTemporaryStat.ElementSoul.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ElementSoul).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ElementSoul).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.GlimmeringTime) || aDefaultFlags.contains(CharacterTemporaryStat.GlimmeringTime)) {
            uFlagTemp[CharacterTemporaryStat.GlimmeringTime.getPosition()] |= CharacterTemporaryStat.GlimmeringTime.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.GlimmeringTime).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.GlimmeringTime).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Reincarnation) || aDefaultFlags.contains(CharacterTemporaryStat.Reincarnation)) {
            uFlagTemp[CharacterTemporaryStat.Reincarnation.getPosition()] |= CharacterTemporaryStat.Reincarnation.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Reincarnation).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Reincarnation).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Beholder) || aDefaultFlags.contains(CharacterTemporaryStat.Beholder)) {
            uFlagTemp[CharacterTemporaryStat.Beholder.getPosition()] |= CharacterTemporaryStat.Beholder.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Beholder).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Beholder).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.QuiverCatridge) || aDefaultFlags.contains(CharacterTemporaryStat.QuiverCatridge)) {
            uFlagTemp[CharacterTemporaryStat.QuiverCatridge.getPosition()] |= CharacterTemporaryStat.QuiverCatridge.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.QuiverCatridge).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.QuiverCatridge).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.ArmorPiercing) || aDefaultFlags.contains(CharacterTemporaryStat.ArmorPiercing)) {
            uFlagTemp[CharacterTemporaryStat.ArmorPiercing.getPosition()] |= CharacterTemporaryStat.ArmorPiercing.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ArmorPiercing).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ArmorPiercing).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.UserControlMob) || aDefaultFlags.contains(CharacterTemporaryStat.UserControlMob)) {
            uFlagTemp[CharacterTemporaryStat.UserControlMob.getPosition()] |= CharacterTemporaryStat.UserControlMob.getValue();
        }
        if (hasStat(CharacterTemporaryStat.ZeroAuraStr) || aDefaultFlags.contains(CharacterTemporaryStat.ZeroAuraStr)) {
            uFlagTemp[CharacterTemporaryStat.ZeroAuraStr.getPosition()] |= CharacterTemporaryStat.ZeroAuraStr.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ZeroAuraStr).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ZeroAuraStr).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.ZeroAuraSpd) || aDefaultFlags.contains(CharacterTemporaryStat.ZeroAuraSpd)) {
            uFlagTemp[CharacterTemporaryStat.ZeroAuraSpd.getPosition()] |= CharacterTemporaryStat.ZeroAuraSpd.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ZeroAuraSpd).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ZeroAuraSpd).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.ImmuneBarrier) || aDefaultFlags.contains(CharacterTemporaryStat.ImmuneBarrier)) {
            uFlagTemp[CharacterTemporaryStat.ImmuneBarrier.getPosition()] |= CharacterTemporaryStat.ImmuneBarrier.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ImmuneBarrier).nOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ImmuneBarrier).xOption, 4));
        }
        // 434
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_434) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_434)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_434.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_434.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_434).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_434).xOption, 4));
        }
        // 435
        if (hasStat(CharacterTemporaryStat.AnimalChange) || aDefaultFlags.contains(CharacterTemporaryStat.AnimalChange)) {
            uFlagTemp[CharacterTemporaryStat.AnimalChange.getPosition()] |= CharacterTemporaryStat.AnimalChange.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AnimalChange).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AnimalChange).xOption, 4));
        }
        // 436
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_436) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_436)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_436.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_436.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_436).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_436).xOption, 4));
        }
        // 474
        if (hasStat(CharacterTemporaryStat.Fever) || aDefaultFlags.contains(CharacterTemporaryStat.Fever)) {
            uFlagTemp[CharacterTemporaryStat.Fever.getPosition()] |= CharacterTemporaryStat.Fever.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Fever).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Fever).xOption, 4));
        }
        // 415
        if (hasStat(CharacterTemporaryStat.AURA_BOOST) || aDefaultFlags.contains(CharacterTemporaryStat.AURA_BOOST)) {
            uFlagTemp[CharacterTemporaryStat.AURA_BOOST.getPosition()] |= CharacterTemporaryStat.AURA_BOOST.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AURA_BOOST).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AURA_BOOST).xOption, 4));
        }

        if (hasStat(CharacterTemporaryStat.FullSoulMP) || aDefaultFlags.contains(CharacterTemporaryStat.FullSoulMP)) {
            uFlagTemp[CharacterTemporaryStat.FullSoulMP.getPosition()] |= CharacterTemporaryStat.FullSoulMP.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FullSoulMP).rOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FullSoulMP).xOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.AntiMagicShell) || aDefaultFlags.contains(CharacterTemporaryStat.AntiMagicShell)) {
            uFlagTemp[CharacterTemporaryStat.AntiMagicShell.getPosition()] |= CharacterTemporaryStat.AntiMagicShell.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AntiMagicShell).nOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.Dance) || aDefaultFlags.contains(CharacterTemporaryStat.Dance)) {
            uFlagTemp[CharacterTemporaryStat.Dance.getPosition()] |= CharacterTemporaryStat.Dance.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Dance).rOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Dance).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.SpiritGuard) || aDefaultFlags.contains(CharacterTemporaryStat.SpiritGuard)) {
            uFlagTemp[CharacterTemporaryStat.SpiritGuard.getPosition()] |= CharacterTemporaryStat.SpiritGuard.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.SpiritGuard).rOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.SpiritGuard).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_441) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_441)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_441.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_441.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_441).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_441).rOption, 4));
        }

        if (hasStat(CharacterTemporaryStat.ComboTempest) || aDefaultFlags.contains(CharacterTemporaryStat.ComboTempest)) {
            uFlagTemp[CharacterTemporaryStat.ComboTempest.getPosition()] |= CharacterTemporaryStat.ComboTempest.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ComboTempest).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ComboTempest).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.HalfstatByDebuff) || aDefaultFlags.contains(CharacterTemporaryStat.HalfstatByDebuff)) {
            uFlagTemp[CharacterTemporaryStat.HalfstatByDebuff.getPosition()] |= CharacterTemporaryStat.HalfstatByDebuff.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HalfstatByDebuff).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HalfstatByDebuff).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.ComplusionSlant) || aDefaultFlags.contains(CharacterTemporaryStat.ComplusionSlant)) {
            uFlagTemp[CharacterTemporaryStat.ComplusionSlant.getPosition()] |= CharacterTemporaryStat.ComplusionSlant.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ComplusionSlant).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ComplusionSlant).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.JaguarSummoned) || aDefaultFlags.contains(CharacterTemporaryStat.JaguarSummoned)) {
            uFlagTemp[CharacterTemporaryStat.JaguarSummoned.getPosition()] |= CharacterTemporaryStat.JaguarSummoned.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.JaguarSummoned).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.JaguarSummoned).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.BMageAura) || aDefaultFlags.contains(CharacterTemporaryStat.BMageAura)) {
            uFlagTemp[CharacterTemporaryStat.BMageAura.getPosition()] |= CharacterTemporaryStat.BMageAura.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BMageAura).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BMageAura).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.元氣覺醒) || aDefaultFlags.contains(CharacterTemporaryStat.元氣覺醒)) {
            uFlagTemp[CharacterTemporaryStat.元氣覺醒.getPosition()] |= CharacterTemporaryStat.元氣覺醒.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.元氣覺醒).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.元氣覺醒).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.能量爆炸) || aDefaultFlags.contains(CharacterTemporaryStat.能量爆炸)) {
            uFlagTemp[CharacterTemporaryStat.能量爆炸.getPosition()] |= CharacterTemporaryStat.能量爆炸.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.能量爆炸).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.能量爆炸).rOption, 4));
        }

        if (hasStat(CharacterTemporaryStat.IDA_BUFF_504) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_504)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_504.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_504.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_504).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_504).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.槍彈盛宴) || aDefaultFlags.contains(CharacterTemporaryStat.槍彈盛宴)) {
            uFlagTemp[CharacterTemporaryStat.槍彈盛宴.getPosition()] |= CharacterTemporaryStat.槍彈盛宴.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.槍彈盛宴).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.槍彈盛宴).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_506) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_506)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_506.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_506.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_506).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_506).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.交換攻擊) || aDefaultFlags.contains(CharacterTemporaryStat.交換攻擊)) {
            uFlagTemp[CharacterTemporaryStat.交換攻擊.getPosition()] |= CharacterTemporaryStat.交換攻擊.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.交換攻擊).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.交換攻擊).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.聖靈祈禱) || aDefaultFlags.contains(CharacterTemporaryStat.聖靈祈禱)) {
            uFlagTemp[CharacterTemporaryStat.聖靈祈禱.getPosition()] |= CharacterTemporaryStat.聖靈祈禱.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.聖靈祈禱).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.聖靈祈禱).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.DarkLighting) || aDefaultFlags.contains(CharacterTemporaryStat.DarkLighting)) {
            uFlagTemp[CharacterTemporaryStat.DarkLighting.getPosition()] |= CharacterTemporaryStat.DarkLighting.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DarkLighting).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.DarkLighting).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.AttackCountX) || aDefaultFlags.contains(CharacterTemporaryStat.AttackCountX)) {
            uFlagTemp[CharacterTemporaryStat.AttackCountX.getPosition()] |= CharacterTemporaryStat.AttackCountX.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AttackCountX).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AttackCountX).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.FireBarrier) || aDefaultFlags.contains(CharacterTemporaryStat.FireBarrier)) {
            uFlagTemp[CharacterTemporaryStat.FireBarrier.getPosition()] |= CharacterTemporaryStat.FireBarrier.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FireBarrier).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FireBarrier).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.KeyDownMoving) || aDefaultFlags.contains(CharacterTemporaryStat.KeyDownMoving)) {
            uFlagTemp[CharacterTemporaryStat.KeyDownMoving.getPosition()] |= CharacterTemporaryStat.KeyDownMoving.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.KeyDownMoving).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.KeyDownMoving).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.MichaelSoulLink) || aDefaultFlags.contains(CharacterTemporaryStat.MichaelSoulLink)) {
            uFlagTemp[CharacterTemporaryStat.MichaelSoulLink.getPosition()] |= CharacterTemporaryStat.MichaelSoulLink.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.MichaelSoulLink).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.MichaelSoulLink).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.KinesisPsychicEnergeShield) || aDefaultFlags.contains(CharacterTemporaryStat.KinesisPsychicEnergeShield)) {
            uFlagTemp[CharacterTemporaryStat.KinesisPsychicEnergeShield.getPosition()] |= CharacterTemporaryStat.KinesisPsychicEnergeShield.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.KinesisPsychicEnergeShield).rOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.KinesisPsychicEnergeShield).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.BladeStance) || aDefaultFlags.contains(CharacterTemporaryStat.BladeStance)) {
            uFlagTemp[CharacterTemporaryStat.BladeStance.getPosition()] |= CharacterTemporaryStat.BladeStance.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BladeStance).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BladeStance).rOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BladeStance).xOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Fever) || aDefaultFlags.contains(CharacterTemporaryStat.Fever)) {
            uFlagTemp[CharacterTemporaryStat.Fever.getPosition()] |= CharacterTemporaryStat.Fever.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Fever).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Fever).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.AdrenalinBoost) || aDefaultFlags.contains(CharacterTemporaryStat.AdrenalinBoost)) {
            uFlagTemp[CharacterTemporaryStat.AdrenalinBoost.getPosition()] |= CharacterTemporaryStat.AdrenalinBoost.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AdrenalinBoost).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.RWBarrierHeal) || aDefaultFlags.contains(CharacterTemporaryStat.RWBarrierHeal)) {
            uFlagTemp[CharacterTemporaryStat.RWBarrierHeal.getPosition()] |= CharacterTemporaryStat.RWBarrierHeal.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.RWBarrierHeal).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.RWMagnumBlow) || aDefaultFlags.contains(CharacterTemporaryStat.RWMagnumBlow)) {
            uFlagTemp[CharacterTemporaryStat.RWMagnumBlow.getPosition()] |= CharacterTemporaryStat.RWMagnumBlow.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.RWMagnumBlow).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.RWBarrier) || aDefaultFlags.contains(CharacterTemporaryStat.RWBarrier)) {
            uFlagTemp[CharacterTemporaryStat.RWBarrier.getPosition()] |= CharacterTemporaryStat.RWBarrier.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.RWBarrier).nOption, 4));
        }

        if (hasStat(CharacterTemporaryStat.IDA_BUFF_249) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_249)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_249.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_249.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_249).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_249).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_250) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_250)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_250.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_250.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_250).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_250).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Stigma) || aDefaultFlags.contains(CharacterTemporaryStat.Stigma)) {
            uFlagTemp[CharacterTemporaryStat.Stigma.getPosition()] |= CharacterTemporaryStat.Stigma.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Stigma).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Stigma).rOption, 4));
        }

        if (hasStat(CharacterTemporaryStat.IDA_BUFF_412) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_412)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_412.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_412.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_412).rOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_412).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_513) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_513)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_513.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_513.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_513).rOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_513).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_514) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_514)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_514.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_514.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_514).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_496) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_496)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_496.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_496.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_496).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.散式投擲) || aDefaultFlags.contains(CharacterTemporaryStat.散式投擲)) {
            uFlagTemp[CharacterTemporaryStat.散式投擲.getPosition()] |= CharacterTemporaryStat.散式投擲.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.散式投擲).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IncMonsterBattleCaptureRate) || aDefaultFlags.contains(CharacterTemporaryStat.IncMonsterBattleCaptureRate)) {
            uFlagTemp[CharacterTemporaryStat.IncMonsterBattleCaptureRate.getPosition()] |= CharacterTemporaryStat.IncMonsterBattleCaptureRate.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IncMonsterBattleCaptureRate).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IncMonsterBattleCaptureRate).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_544) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_544)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_544.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_544.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_544).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_544).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_543) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_543)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_543.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_543.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_543).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_543).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_542) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_542)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_542.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_542.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_542).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_542).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_541) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_541)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_541.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_541.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_541).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_541).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_80) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_80)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_80.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_80.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_80).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_80).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.HayatoStance) || aDefaultFlags.contains(CharacterTemporaryStat.HayatoStance)) {
            uFlagTemp[CharacterTemporaryStat.HayatoStance.getPosition()] |= CharacterTemporaryStat.HayatoStance.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoStance).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoStance).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.HayatoStance) || aDefaultFlags.contains(CharacterTemporaryStat.HayatoStance)) {
            uFlagTemp[CharacterTemporaryStat.HayatoStance.getPosition()] |= CharacterTemporaryStat.HayatoStance.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoStance).tOption, 4));
        }

        if (hasStat(CharacterTemporaryStat.IDA_BUFF_423) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_423)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_423.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_423.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_423).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_423).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.HayatoStanceBonus) || aDefaultFlags.contains(CharacterTemporaryStat.HayatoStanceBonus)) {
            uFlagTemp[CharacterTemporaryStat.HayatoStanceBonus.getPosition()] |= CharacterTemporaryStat.HayatoStanceBonus.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoStanceBonus).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoStanceBonus).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.HayatoPAD) || aDefaultFlags.contains(CharacterTemporaryStat.HayatoPAD)) {
            uFlagTemp[CharacterTemporaryStat.HayatoPAD.getPosition()] |= CharacterTemporaryStat.HayatoPAD.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoPAD).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoPAD).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.HayatoHPR) || aDefaultFlags.contains(CharacterTemporaryStat.HayatoHPR)) {
            uFlagTemp[CharacterTemporaryStat.HayatoHPR.getPosition()] |= CharacterTemporaryStat.HayatoHPR.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoHPR).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoHPR).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.HayatoMPR) || aDefaultFlags.contains(CharacterTemporaryStat.HayatoMPR)) {
            uFlagTemp[CharacterTemporaryStat.HayatoMPR.getPosition()] |= CharacterTemporaryStat.HayatoMPR.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoMPR).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoMPR).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.HayatoCr) || aDefaultFlags.contains(CharacterTemporaryStat.HayatoCr)) {
            uFlagTemp[CharacterTemporaryStat.HayatoCr.getPosition()] |= CharacterTemporaryStat.HayatoCr.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoCr).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoCr).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.FireBarrier) || aDefaultFlags.contains(CharacterTemporaryStat.FireBarrier)) {
            uFlagTemp[CharacterTemporaryStat.FireBarrier.getPosition()] |= CharacterTemporaryStat.FireBarrier.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FireBarrier).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.FireBarrier).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.HayatoBoss) || aDefaultFlags.contains(CharacterTemporaryStat.HayatoBoss)) {
            uFlagTemp[CharacterTemporaryStat.HayatoBoss.getPosition()] |= CharacterTemporaryStat.HayatoBoss.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoBoss).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.HayatoBoss).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.Stance) || aDefaultFlags.contains(CharacterTemporaryStat.Stance)) {
            uFlagTemp[CharacterTemporaryStat.Stance.getPosition()] |= CharacterTemporaryStat.Stance.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Stance).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Stance).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_524) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_524)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_524.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_524.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_524).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_524).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_431) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_431)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_431.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_431.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_431).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_431).rOption, 4));
        }

        // check 432
        if (hasStat(CharacterTemporaryStat.BlackHeartedCurse) || aDefaultFlags.contains(CharacterTemporaryStat.BlackHeartedCurse)) {
            uFlagTemp[CharacterTemporaryStat.BlackHeartedCurse.getPosition()] |= CharacterTemporaryStat.BlackHeartedCurse.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BlackHeartedCurse).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BlackHeartedCurse).rOption, 4));
        }

        if (hasStat(CharacterTemporaryStat.EyeForEye) || aDefaultFlags.contains(CharacterTemporaryStat.EyeForEye)) {
            uFlagTemp[CharacterTemporaryStat.EyeForEye.getPosition()] |= CharacterTemporaryStat.EyeForEye.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.EyeForEye).nOption, 2));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.EyeForEye).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_154) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_154)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_154.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_154.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_154).nOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_154).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_526) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_526)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_526.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_526.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_526).nOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_526).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_527) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_527)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_527.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_527.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_527).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_528) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_528)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_528.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_528.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_528).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_530) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_530)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_530.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_530.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_530).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_531) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_531)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_531.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_531.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_531).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_532) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_532)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_532.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_532.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_532).nOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_532).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_533) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_533)) {
            uFlagTemp[CharacterTemporaryStat.IDA_BUFF_533.getPosition()] |= CharacterTemporaryStat.IDA_BUFF_533.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_533).nOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.PoseType) || aDefaultFlags.contains(CharacterTemporaryStat.PoseType)) {
            uFlagTemp[CharacterTemporaryStat.PoseType.getPosition()] |= CharacterTemporaryStat.PoseType.getValue();
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.PoseType).nOption, 1));
        }

        if (hasStat(CharacterTemporaryStat.BattlePvPHelenaMark) || aDefaultFlags.contains(CharacterTemporaryStat.BattlePvPHelenaMark)) {
            uFlagTemp[CharacterTemporaryStat.BattlePvPHelenaMark.getPosition()] |= CharacterTemporaryStat.BattlePvPHelenaMark.getValue();
        }
        if (hasStat(CharacterTemporaryStat.BattlePvPLangEProtection) || aDefaultFlags.contains(CharacterTemporaryStat.BattlePvPLangEProtection)) {
            uFlagTemp[CharacterTemporaryStat.BattlePvPLangEProtection.getPosition()] |= CharacterTemporaryStat.BattlePvPLangEProtection.getValue();
        }
        uFlagTemp[CharacterTemporaryStat.IndieStatCount.getPosition()] |= CharacterTemporaryStat.IndieStatCount.getValue();
        uFlagTemp[CharacterTemporaryStat.EnergyCharged.getPosition()] |= CharacterTemporaryStat.EnergyCharged.getValue();
        uFlagTemp[CharacterTemporaryStat.DashSpeed.getPosition()] |= CharacterTemporaryStat.DashSpeed.getValue();
        uFlagTemp[CharacterTemporaryStat.DashJump.getPosition()] |= CharacterTemporaryStat.DashJump.getValue();
        uFlagTemp[CharacterTemporaryStat.RideVehicle.getPosition()] |= CharacterTemporaryStat.RideVehicle.getValue();
        uFlagTemp[CharacterTemporaryStat.PartyBooster.getPosition()] |= CharacterTemporaryStat.PartyBooster.getValue();
        uFlagTemp[CharacterTemporaryStat.GuidedBullet.getPosition()] |= CharacterTemporaryStat.GuidedBullet.getValue();
        uFlagTemp[CharacterTemporaryStat.Undead.getPosition()] |= CharacterTemporaryStat.Undead.getValue();
        uFlagTemp[CharacterTemporaryStat.RideVehicleExpire.getPosition()] |= CharacterTemporaryStat.RideVehicleExpire.getValue();
        uFlagTemp[CharacterTemporaryStat.COUNT_PLUS1.getPosition()] |= CharacterTemporaryStat.COUNT_PLUS1.getValue();
        for (int anUFlagTemp : uFlagTemp) {
            outPacket.encodeInt(anUFlagTemp);
        }
        encodeOptionValues(outPacket, uFlagData);
        uFlagData.clear();
        outPacket.encodeByte(getDefenseAtt());
        outPacket.encodeByte(getDefenseState());
        outPacket.encodeByte(getPvpDamage());

        if (hasStat(CharacterTemporaryStat.ZeroAuraStr) || aDefaultFlags.contains(CharacterTemporaryStat.ZeroAuraStr)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ZeroAuraStr).bOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.ZeroAuraSpd) || aDefaultFlags.contains(CharacterTemporaryStat.ZeroAuraSpd)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.ZeroAuraSpd).bOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.BMageAura) || aDefaultFlags.contains(CharacterTemporaryStat.BMageAura)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BMageAura).bOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.BattlePvPHelenaMark) || aDefaultFlags.contains(CharacterTemporaryStat.BattlePvPHelenaMark)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BattlePvPHelenaMark).nOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BattlePvPHelenaMark).rOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BattlePvPHelenaMark).cOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.BattlePvPLangEProtection) || aDefaultFlags.contains(CharacterTemporaryStat.BattlePvPLangEProtection)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BattlePvPLangEProtection).nOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.BattlePvPLangEProtection).rOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.MichaelSoulLink) || aDefaultFlags.contains(CharacterTemporaryStat.MichaelSoulLink)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.MichaelSoulLink).xOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.MichaelSoulLink).bOption, 1));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.MichaelSoulLink).cOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.MichaelSoulLink).yOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.AdrenalinBoost) || aDefaultFlags.contains(CharacterTemporaryStat.AdrenalinBoost)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.AdrenalinBoost).cOption, 1));
        }
        if (hasStat(CharacterTemporaryStat.Stigma) || aDefaultFlags.contains(CharacterTemporaryStat.Stigma)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.Stigma).bOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_412) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_412)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_412).nOption, 2));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_413) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_413)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_413).nOption, 2));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_414) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_414)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_414).nOption, 2));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_513) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_513)) { // Unsure of options
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_513).nOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_513).rOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_513).xOption, 4));
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.IDA_BUFF_513).yOption, 4));
        }
        if (hasStat(CharacterTemporaryStat.VampDeath) || aDefaultFlags.contains(CharacterTemporaryStat.VampDeath)) {
            uFlagData.add(new Tuple<>(getOption(CharacterTemporaryStat.VampDeath).xOption, 4));
        }
        encodeOptionValues(outPacket, uFlagData);
        getStopForceAtom().encode(outPacket);
        outPacket.encodeInt(getViperEnergyCharge());
        for (TSIndex pIndex : TSIndex.values()) {
            getTSBByTSIndex(pIndex).encode(outPacket);
        }

        if (hasStat(CharacterTemporaryStat.IndieStatCount) || aDefaultFlags.contains(CharacterTemporaryStat.IndieStatCount)) {
            this.encodeIndieTempStat(outPacket, getCurrentStats().getOrDefault(CharacterTemporaryStat.IndieStatCount, new ArrayList<>()));
        }
        if (hasStat(CharacterTemporaryStat.IDA_BUFF_544) || aDefaultFlags.contains(CharacterTemporaryStat.IDA_BUFF_544)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_544).xOption);
        }
        if (hasStat(CharacterTemporaryStat.KeyDownMoving) || aDefaultFlags.contains(CharacterTemporaryStat.KeyDownMoving)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.KeyDownMoving).xOption);
        }
        if (hasStat(CharacterTemporaryStat.NewFlying) || aDefaultFlags.contains(CharacterTemporaryStat.NewFlying)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NewFlying).xOption);
        }

    }

    private void encodeOptionValues(OutPacket<GameClient> outPacket, ArrayList<Tuple<Integer, Integer>> uFlagData) {
        for (Tuple<Integer, Integer> nStats : uFlagData) {
            if (null != nStats.getRight()) {
                switch (nStats.getRight()) {
                    case 4:
                        outPacket.encodeInt(nStats.getLeft());
                        break;
                    case 2:
                        outPacket.encodeShort(nStats.getLeft());
                        break;
                    case 1:
                        outPacket.encodeByte(nStats.getLeft());
                        break;
                    default:
                        break;
                }
            }
        }
    }

}
