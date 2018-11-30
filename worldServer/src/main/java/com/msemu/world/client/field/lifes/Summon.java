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

package com.msemu.world.client.field.lifes;

import com.msemu.commons.data.enums.SkillStat;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.summon.LP_SummonEnterField;
import com.msemu.core.network.packets.outpacket.summon.LP_SummonLeaveField;
import com.msemu.world.client.character.AvatarLook;
import com.msemu.world.client.character.Character;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.FieldObjectType;
import com.msemu.world.enums.LeaveType;
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
        client.write(new LP_SummonEnterField(this));
    }

    @Override
    public void outScreen(GameClient client) {
        client.write(new LP_SummonLeaveField(this, LeaveType.NO_ANIMATION));
    }

}