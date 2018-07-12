package com.msemu.world.client.character.stats;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.thread.EventManager;
import com.msemu.commons.utils.types.Tuple;
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
    private StopForceAtom stopForceAtom;
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
                    twoStates.add(new TemporaryStatBase(true));
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
        res.add(new Option());
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

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_495)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_495).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_495).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_495).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_496)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_496).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_496).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_496).tOption);
        }


        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_488)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_488).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_488).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_488).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_489)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_489).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_489).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_489).tOption);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_491)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_491).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_491).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_491).tOption);
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

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_505)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_505).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_505).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_505).tOption);
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
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_534)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_534).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_534).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_534).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_535)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_535).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_535).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_535).tOption);
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
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_441)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_441).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_441).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_441).tOption);
        }
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_442)) {
            outPacket.encodeShort(getOption(CharacterTemporaryStat.IDA_BUFF_442).nOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_442).rOption);
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_442).tOption);
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
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_505)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_505).bOption);
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
        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_523)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.IDA_BUFF_523).bOption);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }

        if (hasNewStat(CharacterTemporaryStat.VampDeath)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.VampDeath).bOption);
        }

        for (int i = 0; i < 7; i++) {
            if (hasNewStat(TSIndex.getCTSFromTwoStatIndex(i))) {
                getTwoStates().get(i).encode(outPacket);
            }
        }
        encodeIndieTempStat(outPacket);
//        if (hasNewStat(UsingScouter)) {
//            outPacket.encodeInt(getOption(UsingScouter).nOption);
//            outPacket.encodeInt(getOption(UsingScouter).xOption);
//        }

        if (hasNewStat(CharacterTemporaryStat.NewFlying)) {
            outPacket.encodeInt(getOption(CharacterTemporaryStat.NewFlying).xOption);
        }

        if (hasNewStat(CharacterTemporaryStat.RideVehicleExpire)) {
            outPacket.encodeByte(1);
            outPacket.encodeByte(1);
        }

        if (hasNewStat(CharacterTemporaryStat.COUNT_PLUS1)) {
            outPacket.encodeByte(0);
            outPacket.encodeByte(1);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_525)) {
            outPacket.encodeByte(1);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_155)) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }

        if (hasNewStat(CharacterTemporaryStat.IDA_BUFF_533)) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }
        if (hasNewStat(CharacterTemporaryStat.COUNT_PLUS1)) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }
        //
    }

    private void encodeIndieTempStat(OutPacket outPacket) {
        Map<CharacterTemporaryStat, List<Option>> stats = getCurrentStats().entrySet().stream()
                .filter(stat -> stat.getKey().isIndie())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (Map.Entry<CharacterTemporaryStat, List<Option>> stat : stats.entrySet()) {
            int curTime = (int) System.currentTimeMillis();
            List<Option> options = stat.getValue();
            if (options == null) {
                outPacket.encodeInt(0);
                continue;
            }
            outPacket.encodeInt(options.size());
            for (Option option : options) {
                outPacket.encodeInt(option.nReason);
                outPacket.encodeInt(option.nValue);
                outPacket.encodeInt(option.nKey); // nKey
                outPacket.encodeInt(curTime - option.tStart);
                outPacket.encodeInt(option.tTerm); // tTerm
                outPacket.encodeInt(0); // size
            }
            int idk = 0;
            outPacket.encodeInt(idk);
            for (int i = 0; i < idk; i++) {
                outPacket.encodeInt(0); // nMValueKey
                outPacket.encodeInt(0); // nMValue
            }
        }
    }

    public void encodeRemovedIndieTempStat(OutPacket outPacket) {
        Map<CharacterTemporaryStat, List<Option>> stats = getRemovedStats().entrySet().stream()
                .filter(stat -> stat.getKey().isIndie())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (Map.Entry<CharacterTemporaryStat, List<Option>> stat : stats.entrySet()) {
            int curTime = (int) System.currentTimeMillis();
            CharacterTemporaryStat key = stat.getKey();
            List<Option> options = getOptions(key);
            if (options == null) {
                outPacket.encodeInt(0);
                continue;
            }
            outPacket.encodeInt(options.size());
            for (Option option : options) {
                outPacket.encodeInt(option.nReason);
                outPacket.encodeInt(option.nValue);
                outPacket.encodeInt(option.nKey); // nKey
                outPacket.encodeInt(curTime - option.tStart);
                outPacket.encodeInt(option.tTerm); // tTerm
                outPacket.encodeInt(0); // size
                // pw.writeInt(0); // nMValueKey
                // pw.writeInt(0); // nMValue
            }
        }
    }


    public void encodeForRemote(OutPacket outPacket) {
        int[] mask = getNewFlags();
        for (int i = 0; i < getNewFlags().length; i++) {
            outPacket.encodeInt(0);
        }
        outPacket.encodeByte(getDefenseAtt());
        outPacket.encodeByte(getDefenseState());
        outPacket.encodeByte(getPvpDamage());
        new StopForceAtom().encode(outPacket);
        outPacket.encodeInt(getViperEnergyCharge());
    }

}
