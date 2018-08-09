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

package com.msemu.world.client.field.runestones;

import com.msemu.commons.data.enums.SkillStat;
import com.msemu.commons.data.templates.field.Foothold;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.thread.EventManager;
import com.msemu.commons.utils.Rand;
import com.msemu.commons.utils.Utils;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.packets.outpacket.field.LP_RuneStoneAppear;
import com.msemu.core.network.packets.outpacket.field.LP_RuneStoneClearAndAllRegister;
import com.msemu.core.network.packets.outpacket.field.LP_RuneStoneDisappear;
import com.msemu.core.network.packets.outpacket.user.local.LP_RuneStoneSkillAck;
import com.msemu.core.network.packets.outpacket.user.local.LP_RuneStoneUseAck;
import com.msemu.core.network.packets.outpacket.user.local.LP_UserRandAreaAttackRequest;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.stats.CharacterTemporaryStat;
import com.msemu.world.client.character.stats.Option;
import com.msemu.world.client.character.stats.TemporaryStatManager;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.client.field.runestones.results.CantUseRuneStoneResult;
import com.msemu.world.client.field.runestones.results.UseRuneStoneSuccessResult;
import com.msemu.world.client.field.runestones.results.WaitToUseRuneStoneResult;
import com.msemu.world.constants.GameConstants;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.ChatMsgType;
import com.msemu.world.enums.RuneStoneType;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RuneStoneManager {

    @Getter
    private final List<RuneStone> runeStones = new ArrayList<>();
    @Getter
    private final Map<Integer, Long> reqRuneStoneTimestamps = new HashMap<>();
    @Getter
    private final Map<Integer, RuneStoneType> reqRuneStoneRecords = new HashMap<>();
    @Getter
    private final ScheduledFuture<?> runeStoneTimer;
    @Getter
    private final Field field;
    @Getter
    private ScheduledFuture thunderTimer;

    public RuneStoneManager(Field field) {
        this.field = field;
        this.runeStoneTimer = EventManager.getInstance().addFixedRateEvent(this::spawnTask, GameConstants.RUNE_RESPAWN_TIME, GameConstants.RUNE_RESPAWN_TIME, TimeUnit.MINUTES);
    }

    public void registerAll() {
        getField().broadcastPacket(new LP_RuneStoneClearAndAllRegister(getRuneStones()));
    }

    public void showAll() {
        getRuneStones().forEach(runeStone -> {
            getField().broadcastPacket(new LP_RuneStoneAppear(runeStone));
        });
    }

    public void renewRuneStones() {
        getRuneStones().clear();
        List<RuneStoneType> typesCanSpawn = Arrays.stream(RuneStoneType.values())
                .filter(type -> !getRuneStones().stream()
                        .map(RuneStone::getType)
                        .collect(Collectors.toList())
                        .contains(type) && type != RuneStoneType.RST_NONE)
                .collect(Collectors.toList());
        for (int i = 0; i < GameConstants.MAX_RUNESTONE_PER_FIELD; i++) {
            Foothold fh = getField().getRandomFoothold();
            Collections.shuffle(typesCanSpawn);
            if (!typesCanSpawn.isEmpty() && fh != null) {
                RuneStoneType type = RuneStoneType.RST_THUNDER;//typesCanSpawn.get(0);
                RuneStone toSpawn = new RuneStone(type, getRuneStones().size() + 1);
                toSpawn.setPosition(new Position(fh.getX1(), fh.getYFromX(fh.getX1())));
                toSpawn.setFlip(false);
                getRuneStones().add(toSpawn);
            }
        }
        registerAll();
    }

    public void spawnTask() {

        if (getField().getAllMobs().isEmpty() || getField().getBossMobID() > 0)
            return;

        boolean allEnable = getRuneStones().stream().allMatch(RuneStone::isEnable);

        if (allEnable)
            renewRuneStones();

        final RuneStone toSpawn = getRuneStones().stream().filter(runeStone -> !runeStone.isEnable())
                .findAny().orElse(null);

        if (toSpawn != null) {
            toSpawn.setEnable(true);
            getField().broadcastPacket(new LP_RuneStoneAppear(toSpawn));
        }

    }

    public void requestRuneStone(Character chr, RuneStoneType type) {
        final TemporaryStatManager tsm = chr.getTemporaryStatManager();
        List<RuneStone> runeStones = getRuneStones();
        long currentTime = System.currentTimeMillis();
        long lastReqTime = getReqRuneStoneTimestamps().getOrDefault(chr.getId(), 0L);

        // 移除之前的紀錄
        getReqRuneStoneRecords().remove(chr.getId());

        if (currentTime - lastReqTime < GameConstants.RUNE_STONE_REQUEST_COOLTIME) {
            chr.write(new LP_RuneStoneUseAck(new WaitToUseRuneStoneResult((int) (lastReqTime + GameConstants.RUNE_STONE_REQUEST_COOLTIME - currentTime))));
            return;
        }

        RuneStone runeStone = runeStones.stream()
                .filter(item -> item.getType() == type)
                .findFirst().orElse(null);

        if (runeStone != null && !tsm.hasStat(CharacterTemporaryStat.IDA_BUFF_536)) {  // TODO check buff time
            getReqRuneStoneTimestamps().put(chr.getId(), currentTime);
            getReqRuneStoneRecords().put(chr.getId(), type);
            chr.write(new LP_RuneStoneUseAck(new UseRuneStoneSuccessResult()));
            chr.showDebugMessage("符文輪系統", ChatMsgType.GAME_DESC, String.format("要求成功 (%s)", type));
        } else {
            chr.write(new LP_RuneStoneUseAck(new CantUseRuneStoneResult()));
        }
    }

    public void useRuneStone(Character chr) {

        // 沒找到之前要求成功的符文輪
        if (!getReqRuneStoneRecords().containsKey(chr.getId())) {
            chr.enableActions();
            return;
        }

        if (!getReqRuneStoneRecords().containsKey(chr.getId())) {
            return;
        }

        RuneStoneType type = getReqRuneStoneRecords().get(chr.getId());
        RuneStone runeStone = getRuneStones()
                .stream().filter(i -> i.getType().equals(type))
                .findFirst().orElse(null);
        if (runeStone != null) {
            getReqRuneStoneRecords().remove(chr.getId());
            getReqRuneStoneTimestamps().remove(chr.getId());
            getRuneStones().remove(runeStone);
            activateRuneStoneEffect(chr, type);
            getField().broadcastPacket(new LP_RuneStoneDisappear(runeStone, chr, true));
            getField().broadcastPacket(new LP_RuneStoneSkillAck(runeStone));
            registerAll();
            chr.showDebugMessage("符文輪系統", ChatMsgType.GAME_DESC, String.format("使用成功 (%s)", type));
        }
    }

    private void activateRuneStoneEffect(Character chr, RuneStoneType type) {

        final TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillId;
        SkillInfo si;
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();


        switch (type) {
            case RST_UPGRADE_SPEED:
                skillId = 80001427;
                si = SkillData.getInstance().getSkillInfoById(skillId);
                o1.nReason = skillId;
                o1.nValue = si.getValue(SkillStat.indieBooster, 1);
                o1.tStart = ((Long) System.currentTimeMillis()).intValue();
                o1.tTerm = si.getValue(SkillStat.time, 1);
                o2.nReason = skillId;
                o2.nValue = si.getValue(SkillStat.indieJump, 1);
                o2.tStart = ((Long) System.currentTimeMillis()).intValue();
                o2.tTerm = si.getValue(SkillStat.time, 1);
                o3.nReason = skillId;
                o3.nValue = si.getValue(SkillStat.indieSpeed, 1);
                o3.tStart = ((Long) System.currentTimeMillis()).intValue();
                o3.tTerm = si.getValue(SkillStat.time, 1);
                tsm.putCharacterStatValue(CharacterTemporaryStat.IndieBooster, o1);
                tsm.putCharacterStatValue(CharacterTemporaryStat.IndieJump, o2);
                tsm.putCharacterStatValue(CharacterTemporaryStat.IndieSpeed, o3);
                tsm.sendSetStatPacket();
                break;
            case RST_UPGRADE_DEFENCE:
                skillId = 80001428;
                si = SkillData.getInstance().getSkillInfoById(skillId);
                o1.rOption = skillId;
                o1.nOption = si.getValue(SkillStat.ignoreMobDamR, 1);
                o1.tOption = si.getValue(SkillStat.time, 1);
                o2.nReason = skillId;
                o2.nValue = si.getValue(SkillStat.indieAsrR, 1);
                o2.tStart = ((Long) System.currentTimeMillis()).intValue();
                o2.tTerm = si.getValue(SkillStat.time, 1);
                o3.nReason = skillId;
                o3.nValue = si.getValue(SkillStat.indieTerR, 1);
                o3.tStart = ((Long) System.currentTimeMillis()).intValue();
                o3.tTerm = si.getValue(SkillStat.time, 1);
                tsm.putCharacterStatValue(CharacterTemporaryStat.IgnoreMobDamR, o1);
                tsm.putCharacterStatValue(CharacterTemporaryStat.IndieAsrR, o2);
                tsm.putCharacterStatValue(CharacterTemporaryStat.IndieTerR, o3);
                tsm.sendSetStatPacket();
                break;
            case RST_DOT_ATTACK:
                break;
            case RST_THUNDER:
                skillId = 80001756;
                si = SkillData.getInstance().getSkillInfoById(skillId);
                o1.rOption = 80001762;
                o1.nOption = si.getValue(SkillStat.x, 1);
                o1.tOption = si.getValue(SkillStat.time, 1);
                tsm.putCharacterStatValue(CharacterTemporaryStat.RandAreaAttack, o1);
                tsm.sendSetStatPacket();
                int fieldID = chr.getFieldID();
                randAreaAttack(fieldID, tsm, chr);
                break;
            case RST_EARTHQUAKE:
                skillId = 80001757;
                si = SkillData.getInstance().getSkillInfoById(skillId);
                o1.rOption = skillId;
                o1.nOption = si.getValue(SkillStat.x, 1);
                o1.tOption = si.getValue(SkillStat.time, 1);
                tsm.putCharacterStatValue(CharacterTemporaryStat.Inflation, o1);
                break;
            case RST_SUMMON_ELITE_MOB:
                Field field = chr.getField();
                int numberOfEliteMobsSpawned = GameConstants.DARKNESS_RUNE_NUMBER_OF_ELITE_MOBS_SPAWNED;
                for (int i = 0; i < numberOfEliteMobsSpawned; i++) {
                    Mob mob = Utils.getRandomFromList(field.getAllMobs());
                    //  mob.spawnEliteMobRuneOfDarkness(); TODO
                }
                break;
        }
        skillId = 80002280; // 解放的輪之力
        si = SkillData.getInstance().getSkillInfoById(skillId);
        o4.nReason = skillId;
        o4.nKey = Rand.nextInt();
        o4.nValue = si.getValue(SkillStat.indieExp, 1);
        o4.tTerm = si.getValue(SkillStat.time, 1);
        o4.tStart = (int) System.currentTimeMillis();
        tsm.putCharacterStatValue(CharacterTemporaryStat.IndieEXP, o4);
        skillId = 80002282;
        si = SkillData.getInstance().getSkillInfoById(skillId);
        o5.rOption = 80002282;
        o5.nOption = 1;
        o5.tOption = 15 * 60;
        tsm.putCharacterStatValue(CharacterTemporaryStat.IDA_BUFF_536, o5);
        tsm.sendSetStatPacket();
        // 80002303 BUFF
    }


    private void randAreaAttack(int fieldID, TemporaryStatManager tsm, Character chr) {
        if ((tsm.getOptByCTSAndSkill(CharacterTemporaryStat.RandAreaAttack, 80001762) == null) || fieldID != chr.getFieldID() || !chr.isOnline()) {
            return;
        }
        Mob randomMob = Utils.getRandomFromList(chr.getField().getAllMobs());
        chr.write(new LP_UserRandAreaAttackRequest(Collections.singletonList(randomMob), 80001762));
        if (thunderTimer != null && !thunderTimer.isDone()) {
            thunderTimer.cancel(true);
        }
        thunderTimer = EventManager.getInstance().addEvent(() -> randAreaAttack(fieldID, tsm, chr), GameConstants.THUNDER_RUNE_ATTACK_DELAY, TimeUnit.SECONDS);
    }
}
