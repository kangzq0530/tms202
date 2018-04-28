package com.msemu.commons.data.templates;

import com.msemu.commons.data.enums.Element;
import com.msemu.commons.data.enums.ElementalEffectiveness;
import com.msemu.commons.data.loader.dat.DatSerializable;
import com.msemu.commons.utils.types.Tuple;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/4/23.
 */
@Getter
@Setter
public class MobTemplate implements DatSerializable{
    private String name = "", banMsg = "", banMapPortalName = "";
    private int id;
    private long maxHP, finalmaxHP;
    private int maxMP, removeAfter, fixedDamage = -1;
    private short level = 1, charismaEXP, wp;
    private int PADamage, PDDamage, MADamage, MDDamage, partyBonusR, buff = -1, getCP, point, dropItemPeriod, PDRate, MDRate, acc, eva;
    private int summonType, category, speed, pushed;
    private int selfDestructionHP, selfDestructionRemoveAfter, selfDestructionAction = -1;
    private int hpTagColor, hpTagBgcolor;
    private int banMapFieldId;
    private List<Integer> revive = new ArrayList<>();
    private List<Tuple<Integer, Integer>> skills = new ArrayList<>();
    private Map<Element, ElementalEffectiveness> basicElemAttrs = new HashMap<>();
    private byte rareItemDropLevel;
    private boolean firstAttack, ignoreFieldOut, isRemoteRange, ignoreMoveImpact, skeleton, hideUserDamage, individualReward, notConsideredFieldSet;
    private boolean onlyNormalAttack, boss, explosiveReward, undead, escort, partyBonusMob, changeableMob;
    private boolean damagedByMob, noDoom, publicReward;
    private int link;

    public void addRevive(Integer templateId) {
        getRevive().add(templateId);
    }

    public void addSkill(int skillID, int level) {
        getSkills().add(new Tuple<>(skillID, level));
    }

    private void addElementAttr(Element element, ElementalEffectiveness effect) {
        getBasicElemAttrs().put(element, effect);
    }

    @Override
    public String toString() {
        return String.format("[怪物] %s(%d) BOSS: %s", getName(), getId(), isBoss());
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(this.name);
        dos.writeUTF(this.banMsg);
        dos.writeUTF(this.banMapPortalName);
        dos.writeShort(this.level);
        dos.writeShort(this.charismaEXP);
        dos.writeShort(this.wp);
        dos.writeInt(this.id);
        dos.writeLong(this.maxHP);
        dos.writeLong(this.finalmaxHP);
        dos.writeInt(this.maxMP);
        dos.writeInt(this.removeAfter);
        dos.writeInt(this.fixedDamage);
        dos.writeInt(this.PADamage);
        dos.writeInt(this.PDDamage);
        dos.writeInt(this.MADamage);
        dos.writeInt(this.MDDamage);
        dos.writeInt(this.MDRate);
        dos.writeInt(this.MDRate);
        dos.writeInt(this.partyBonusR);
        dos.writeInt(this.buff);
        dos.writeInt(this.getCP);
        dos.writeInt(this.point);
        dos.writeInt(this.dropItemPeriod);
        dos.writeInt(this.acc);
        dos.writeInt(this.eva);
        dos.writeInt(this.summonType);
        dos.writeInt(this.category);
        dos.writeInt(this.speed);
        dos.writeInt(this.pushed);
        dos.writeInt(this.selfDestructionHP);
        dos.writeInt(this.selfDestructionAction);
        dos.writeInt(this.selfDestructionRemoveAfter);
        dos.writeInt(this.hpTagColor);
        dos.writeInt(this.hpTagBgcolor);
        dos.writeInt(this.banMapFieldId);
        dos.writeInt(this.link);
        dos.writeByte(this.rareItemDropLevel);
        dos.writeBoolean(this.firstAttack);
        dos.writeBoolean(this.ignoreFieldOut);
        dos.writeBoolean(this.isRemoteRange);
        dos.writeBoolean(this.ignoreMoveImpact);
        dos.writeBoolean(this.hideUserDamage);
        dos.writeBoolean(this.individualReward);
        dos.writeBoolean(this.notConsideredFieldSet);
        dos.writeBoolean(this.onlyNormalAttack);
        dos.writeBoolean(this.boss);
        dos.writeBoolean(this.explosiveReward);
        dos.writeBoolean(this.undead);
        dos.writeBoolean(this.escort);
        dos.writeBoolean(this.partyBonusMob);
        dos.writeBoolean(this.changeableMob);
        dos.writeBoolean(this.damagedByMob);
        dos.writeBoolean(this.noDoom);
        dos.writeBoolean(this.publicReward);
        dos.writeInt(revive.size());
        for(Integer value : revive)
            dos.writeInt(value);
        dos.writeInt(skills.size());
        for(Tuple<Integer, Integer> tuple : skills) {
            dos.writeInt(tuple.getLeft());
            dos.writeInt(tuple.getRight());
        }
        dos.writeInt(basicElemAttrs.size());
        for(Map.Entry<Element, ElementalEffectiveness> entry: basicElemAttrs.entrySet()) {
            dos.writeInt(entry.getKey().getValue());
            dos.writeInt(ElementalEffectiveness.getNumber(entry.getValue()));
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        return this;
    }
}
