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
