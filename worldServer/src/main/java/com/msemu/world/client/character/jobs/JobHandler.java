package com.msemu.world.client.character.jobs;

import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_StatChanged;
import com.msemu.world.client.character.AttackInfo;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.HitInfo;
import com.msemu.world.client.character.SkillUseInfo;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.constants.SkillConstants;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.Stat;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/4/12.
 */
public abstract class JobHandler {

    @Getter
    private Character character;

    public JobHandler(Character character) {
        this.character = character;
    }

    public GameClient getClient() {
        return this.character.getClient();
    }

    public abstract boolean handleAttack(AttackInfo attackInfo);

    public abstract boolean handleSkillUse(SkillUseInfo skillUseInfo);

    public abstract boolean isHandlerOfJob(short id);

    public abstract int getFinalAttackSkill();

    public abstract boolean isBuff(int skillID);

    public abstract void handleHitPacket(InPacket inPacket, HitInfo hitInfo);

    public void handleHitPacket(InPacket inPacket) {
        inPacket.decodeInt(); // tick
        int idk1 = inPacket.decodeInt();
        byte idk2 = inPacket.decodeByte(); // -1?
        byte idk3 = inPacket.decodeByte();
        int damage = inPacket.decodeInt();
        short idk4 = inPacket.decodeShort();
        int templateID = inPacket.decodeInt();
        int mobID = inPacket.decodeInt();
        HitInfo hitInfo = new HitInfo();
        hitInfo.setHPDamage(damage);
        hitInfo.setTemplateID(templateID);
        hitInfo.setMobID(mobID);
        handleHitPacket(inPacket, hitInfo);
        handleHit(hitInfo);
    }

    public void handleHit(HitInfo hitInfo) {
//        Character character = getClient().getCharacter();
//        int curHP = character.getStat(Stat.HP);
//        int newHP = curHP - hitInfo.getHPDamage();
//        if(newHP <= 0) {
//            // TODO Dying
//            curHP = character.getStat(Stat.MAX_HP);
//        } else {
//            curHP = newHP;
//        }
//        Map<Stat, Object> stats = new HashMap<>();
//        character.setStat(Stat.HP, curHP);
//        stats.put(Stat.HP, curHP);
//
//        int curMP = character.getStat(Stat.MP);
//        int newMP = curMP - hitInfo.getMPDamage();
//
//        if(newMP >= 0) {
//            curMP = newMP;
//        }
//
//        character.setStat(Stat.MP, curMP);
//        stats.put(Stat.MP, curMP);
//        getClient().write(wvscontext.statChanged(stats));
    }

    protected SkillInfo getSkillInfo(int skillID) {
        return SkillData.getInstance().getSkillInfoById(skillID);
    }

    public void handleLevelUp() {
        final Character chr = getCharacter();
        final int level = chr.getLevel();
        int sp = 3;
        if (level > 100 && (level % 10) % 3 == 0) {
            sp = 6; // double sp on levels ending in 3/6/9
        }
        chr.addSp(sp);
        Map<Stat, Integer> stats = new HashMap<>();
        stats.put(Stat.MAX_HP, 500);
        stats.put(Stat.MAX_MP, 500);
        chr.renewCharacterStats();
        stats.put(Stat.HP, chr.getCurrentMaxHp());
        stats.put(Stat.MP, chr.getCurrentMaxMp());
        stats.put(Stat.AP, 5);
        chr.addStat(stats);
        byte linkSkillLevel = (byte) SkillConstants.getLinkSkillLevelByCharLevel(level);
        int linkSkillID = SkillConstants.getOriginalOfLinkedSkill(SkillConstants.getLinkSkillByJob(chr.getJob()));
        if (linkSkillID != 0 && linkSkillLevel > 0) {
            Skill skill = chr.getSkill(linkSkillID, true);
            if (skill.getCurrentLevel() != linkSkillLevel) {
                chr.addSkill(linkSkillID, linkSkillLevel, 3);
            }
        }

    }


}
