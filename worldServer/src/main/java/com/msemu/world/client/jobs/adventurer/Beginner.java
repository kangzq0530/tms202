package com.msemu.world.client.jobs.adventurer;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.world.client.character.AttackInfo;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.HitInfo;
import com.msemu.world.client.jobs.JobHandler;
import com.msemu.world.constants.MapleJob;

/**
 * Created by Weber on 2018/4/23.
 */
public class Beginner extends JobHandler {
    public Beginner(Character character) {
        super(character);
    }

    @Override
    public void handleAttack(AttackInfo attackInfo) {

    }

    @Override
    public void handleSkillPacket(int skillID, byte slv, InPacket inPacket) {

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
}
