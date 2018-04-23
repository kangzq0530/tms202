package com.msemu.world.client.jobs;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.AttackInfo;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.HitInfo;
import com.msemu.world.client.character.skills.SkillInfo;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.Stat;

/**
 * Created by Weber on 2018/4/12.
 */
public abstract class Job {

    private Character character;

    public Job(Character character) {
        this.character = character;
    }

    public GameClient getClient() {
        return this.character.getClient();
    }

    public Character getCharacter() {
        return character;
    }


    public abstract void handleAttack(AttackInfo attackInfo);

    public abstract void handleSkillPacket(int skillID, byte slv, InPacket inPacket);

    public abstract boolean isHandlerOfJob(short id);

    public abstract int getFinalAttackSkill();

    public abstract boolean isBuff(int skillID);


    public abstract void handleHitPacket(InPacket inPacket, HitInfo hitInfo);

    public void handleHitPacket(InPacket inPacket){
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

    public void handleHit( HitInfo hitInfo){
//        Character character = getClient().getCharacter();
//        int curHP = character.getStat(Stat.HP);
//        int newHP = curHP - hitInfo.getHPDamage();
//        if(newHP <= 0) {
//            // TODO Dying
//            curHP = character.getStat(Stat.MAXHP);
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
//        getClient().write(WvsContext.statChanged(stats));
    }

    public SkillInfo getInfo(int skillID) {
        return SkillData.getInstance().getSkillInfoById(skillID);
    }

    public void handleLevelUp() {
        getCharacter().addStat(Stat.MAXHP, 500);
        getCharacter().addStat(Stat.MAXMP, 500);
        getCharacter().addStat(Stat.AP, 5);
    }


}
