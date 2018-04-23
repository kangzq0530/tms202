package com.msemu.commons.data;

import com.msemu.commons.data.templates.Foothold;
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
    protected String name;
    protected int id, returnMap, forcedReturn;
    protected int mobRate;
    protected int reactorShuffle, fixedMobCapacity, createMobInterval, timeOut, timeLimit, lvLimit, lvForceMove, consumeItemCoolTime, link;
    protected boolean town, swim, fly, isNeedSkillForFly, partyOnly, expeditionOnly;
    protected String onFirstUserEnter, onUserEnter;
    protected Set<Foothold> footholds = new HashSet<>();


}
