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

package com.msemu.world.client.character.stats;

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
