package com.msemu.commons.data.templates;

import com.msemu.commons.data.enums.Element;
import com.msemu.commons.data.enums.ElementalEffectiveness;
import com.msemu.commons.utils.types.Tuple;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/4/23.
 */
@Getter
@Setter
public class MobTemplate {
    private String name;
    private int id;
    private long maxHP, finalmaxHP;
    private int maxMP, removeAfter, fixedDamage = -1;
    private short level = 1, charismaEXP, wp;
    private int PADamage, PDDamage, MADamage, MDDamage, partyBonusR, buff = -1, getCP, point, dropItemPeriod, PDRate, MDRate, acc, eva;
    private int summonType, category, speed, pushed;
    private int selfDestructionHP, selfDestructionRemoveAfter, selfDestructionAction = -1;
    private int hpTagColor, hpTagBgcolor;
    private String banMsg = "";
    private int banMapFieldId;
    private String banMapPortalName;

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
}
