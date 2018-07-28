package com.msemu.world.client.field.runestones;

import com.msemu.commons.data.enums.SkillStat;
import com.msemu.commons.data.templates.field.Foothold;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.thread.EventManager;
import com.msemu.commons.utils.Rand;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.packets.outpacket.field.LP_RuneStoneClearAndAllRegister;
import com.msemu.core.network.packets.outpacket.field.LP_RuneStoneDisappear;
import com.msemu.core.network.packets.outpacket.user.local.LP_RuneStoneSkillAck;
import com.msemu.core.network.packets.outpacket.user.local.LP_RuneStoneUseAck;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.stats.CharacterTemporaryStat;
import com.msemu.world.client.character.stats.Option;
import com.msemu.world.client.character.stats.TemporaryStatManager;
import com.msemu.world.client.field.Field;
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

    public RuneStoneManager(Field field) {
        this.field = field;
        this.runeStoneTimer = EventManager.getInstance().addFixedRateEvent(this::spawnTask, GameConstants.RUNE_RESPAWN_TIME, GameConstants.RUNE_RESPAWN_TIME, TimeUnit.MINUTES);
    }

    public void registerAll() {
        if (!getRuneStones().isEmpty())
            getField().broadcastPacket(new LP_RuneStoneClearAndAllRegister(getRuneStones()));
    }

    public void spawnTask() {

        if (getField().getAllMobs().isEmpty() || getField().getBossMobID() > 0)
            return;
        if (getRuneStones().size() >= GameConstants.MAX_RUNESTONE_PER_FIELD)
            return;

        final RuneStone toSpawn;

        List<RuneStoneType> typesCanSpawn = Arrays.stream(RuneStoneType.values())
                .filter(type -> !getRuneStones().stream()
                        .map(RuneStone::getType)
                        .collect(Collectors.toList())
                        .contains(type) && type != RuneStoneType.RST_NONE)
                .collect(Collectors.toList());
        Foothold fh = getField().getRandomFoothold();
        Collections.shuffle(typesCanSpawn);
        if (!typesCanSpawn.isEmpty() && fh != null) {
            RuneStoneType type = typesCanSpawn.get(0);
            toSpawn = new RuneStone(type, getRuneStones().size() + 1);
            toSpawn.setPosition(new Position(fh.getX1(), fh.getYFromX(fh.getX1())));
            toSpawn.setFlip(false);
            getRuneStones().add(toSpawn);
            registerAll();
        }
    }

    public void requestRuneStone(Character chr, RuneStoneType type) {
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

        if (runeStone != null) {  // TODO check buff time
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
            activateRuneStoneEffect(chr, type);
            getField().broadcastPacket(new LP_RuneStoneSkillAck(runeStone));
            getField().broadcastPacket(new LP_RuneStoneDisappear(runeStone, chr, true));
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
                break;
            case RST_SUMMON_ELITE_MOB:

                break;
        }
        tsm.sendSetStatPacket();
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
        tsm.putCharacterStatValue(CharacterTemporaryStat.IDA_BUFF_537, o5);
        tsm.sendSetStatPacket();

        // 80002303 BUFF
    }
}
