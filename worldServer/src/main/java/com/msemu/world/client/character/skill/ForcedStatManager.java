package com.msemu.world.client.character.skill;

import com.msemu.core.network.packets.outpacket.wvscontext.LP_ForcedStatReset;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_ForcedStatSet;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.ForcedStat;
import com.msemu.world.enums.Stat;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Weber on 2018/5/12.
 */
public class ForcedStatManager {

    @Getter
    private Character character;

    public ForcedStatManager(Character character) {
        this.character = character;
    }

    public Map<ForcedStat, Integer> getMaxForcedStats() {
        Map<ForcedStat, Integer> stats = new EnumMap<>(ForcedStat.class);
        stats.put(ForcedStat.STR, 999);
        stats.put(ForcedStat.DEX, 999);
        stats.put(ForcedStat.INT, 999);
        stats.put(ForcedStat.LUK, 999);
        stats.put(ForcedStat.WATK, 255);
        stats.put(ForcedStat.ACC, 999);
        stats.put(ForcedStat.AVOID, 999);
        stats.put(ForcedStat.SPEED, 140);
        stats.put(ForcedStat.JUMP, 120);
        return stats;
    }

    public Map<ForcedStat, Integer> getBalrogBossMapLimitStats() {
        Map<ForcedStat, Integer> stats = new EnumMap<>(ForcedStat.class);
        int offset = 1 + (getCharacter().getLevel() - 90) / 20;
        stats.put(ForcedStat.STR, character.getStat(Stat.STR) / offset);
        stats.put(ForcedStat.DEX, character.getStat(Stat.DEX) / offset);
        stats.put(ForcedStat.INT, character.getStat(Stat.INT) / offset);
        stats.put(ForcedStat.LUK, character.getStat(Stat.LUK) / offset);
        // TODO 這邊要算攻擊力
//        stats.put(ForcedStat.WATK, character.getStat(Stat.STR) / offset);
//        stats.put(ForcedStat.MATK, character.getStat(Stat.STR) / offset);
        return stats;
    }


    public void onEnterField(int fieldID) {
        // 暫時能力值
        switch (fieldID) {
            case 940001010://凱薩
            case 931050930://傑諾
            case 927020010://夜光
            case 927000000://惡魔殺手 惡魔復仇者
            case 914000000://狂郎勇士
            case 910150003://精靈
            case 807100102://陰陽師
            case 807100001://劍豪
                getCharacter().write(new LP_ForcedStatSet(getMaxForcedStats()));
                break;
            case 931050040://惡魔殺手 惡魔復仇者
            case 927020071://夜光
            case 910150002://精靈
            case 807000000://陰陽師
            case 807100002://劍豪
            case 400000000://凱薩
            case 310010000://傑諾
            case 140090000://狂郎勇士
            case 105100401://巴洛谷
            case 105100301://巴洛谷
            case 105100100://巴洛谷
                getCharacter().write(new LP_ForcedStatReset());
                break;
            case 105100300://巴洛谷 限制
                getCharacter().write(new LP_ForcedStatSet(getBalrogBossMapLimitStats()));
                break;
        }
    }
}
