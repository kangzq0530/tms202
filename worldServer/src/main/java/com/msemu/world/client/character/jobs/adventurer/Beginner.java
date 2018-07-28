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

package com.msemu.world.client.character.jobs.adventurer;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.Rand;
import com.msemu.world.client.character.AttackInfo;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.HitInfo;
import com.msemu.world.client.character.SkillUseInfo;
import com.msemu.world.client.character.jobs.JobHandler;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.enums.Stat;

/**
 * Created by Weber on 2018/4/23.
 */
public class Beginner extends JobHandler {
    public Beginner(Character character) {
        super(character);
    }

    @Override
    public boolean handleAttack(AttackInfo attackInfo) {
        return true;
    }

    @Override
    public boolean handleSkillUse(SkillUseInfo skillUseInfo) {
        return true;
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return id == MapleJob.初心者.getId();
    }

    @Override
    public int getFinalAttackSkill() {
        return 0;
    }

    @Override
    public boolean isBuff(int skillID) {
        return false;
    }

    @Override
    public void handleHitPacket(InPacket inPacket, HitInfo hitInfo) {

    }

    @Override
    public void handleLevelUp() {
        int addedMaxHp = Rand.get(52, 56);
        getCharacter().addStat(Stat.AP, 5);
        getCharacter().addStat(Stat.MAX_HP, addedMaxHp);
        getCharacter().addStat(Stat.MAX_MP, 30);
    }
}
