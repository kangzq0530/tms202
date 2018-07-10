package com.msemu.world.client.field.lifes;

import com.msemu.commons.data.enums.SkillStat;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.AvatarLook;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.FieldObjectType;
import com.msemu.world.enums.Stat;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/11.
 */
public class Summon extends Life {

    @Getter
    List<Position> teslaCoilPositions = new ArrayList<>();
    @Getter
    @Setter
    private int templateId, charID, skillID, bulletID, maxHP, hp;
    @Getter
    private int summonTerm;
    @Getter
    @Setter
    private byte charLevel, slv, assistType, enterType, teslaCoilState, moveAbility;
    @Getter
    @Setter
    private boolean flyMob, beforeFirstAttack, jaguarActive, attackActive;
    @Getter
    @Setter
    private short curFoothold;
    @Getter
    @Setter
    private AvatarLook avatarLook;
    @Getter
    @Setter
    private Position[] kishinPositions = new Position[2];

    public Summon(int objectId) {
        setObjectId(objectId);
    }

    public static Summon getSummonBy(Character character, int skillID, byte slv) {
        SkillInfo si = SkillData.getInstance().getSkillInfoById(skillID);
        Summon summon = new Summon(-1);
        summon.setCharID(character.getId());
        summon.setSkillID(skillID);
        summon.setSlv(slv);
        summon.setSummonTerm(si.getValue(SkillStat.time, slv));
        summon.setCharLevel((byte) character.getStat(Stat.LEVEL));
        summon.getPosition().setX(character.getPosition().getX());
        summon.getPosition().setY(character.getPosition().getY());
        summon.setAction((byte) 1);
        summon.setCurFoothold((short) character.getField().findFootHoldBelow(summon.getPosition()).getId());
        summon.setMoveAbility((byte) 1);
        summon.setAssistType((byte) 1);
        summon.setEnterType((byte) 1);
        summon.setBeforeFirstAttack(false);
        summon.setSkillID(skillID);
        summon.setAttackActive(true);
        return summon;
    }

    public void setSummonTerm(int summonTerm) {
        this.summonTerm = 1000 * summonTerm;
    }

    @Override
    public FieldObjectType getFieldObjectType() {
        return null;
    }

    @Override
    public void enterScreen(GameClient client) {

    }

    @Override
    public void outScreen(GameClient client) {

    }

}