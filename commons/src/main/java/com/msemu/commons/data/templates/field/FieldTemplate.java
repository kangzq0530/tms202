package com.msemu.commons.data.templates.field;

import com.msemu.commons.utils.types.Rect;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Weber on 2018/4/23.
 */
@Getter
@Setter
public class FieldTemplate {
    private Rect rect = new Rect();
    protected String name;
    protected int id, returnMap, forcedReturn, fieldLimit;
    protected int mobRate, partyBonusR;
    protected int fixedMobCapacity, createMobInterval, timeOut, timeLimit, lvLimit, lvForceMove, consumeItemCoolTime, link;
    protected boolean town, swim, fly, isNeedSkillForFly, partyOnly, expeditionOnly, reactorShuffle;
    protected String onFirstUserEnter, onUserEnter;
    protected FootholdTree footholdTree = null;
    protected Set<Portal> portals = new HashSet<>();
    protected Set<LifeData> life = new HashSet<>();

    public void addPortal(Portal portal) {
        portals.add(portal);
    }

    public void addLifeData(LifeData lifeData) {
        life.add(lifeData);
    }
}
