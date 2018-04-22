package com.msemu.commons.data.enums;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/13.
 */
public enum ItemSpecStat {
    hp,
    mp,
    hpR,
    mpR,
    eva,
    time,
    speed,
    mad,
    pad,
    acc,
    pdd,
    mdd,
    jump,
    imhp,
    immp,
    indieAllStat,
    indieSpeed,
    indieSTR,
    indieDEX,
    indieINT,
    indieLUK,
    indiePad,
    indiePdd,
    indieMad,
    indieMdd,
    indieBDR,
    indieDamR,
    indieIgnoreMobpdpR,
    indieStatR,
    indieMhp,
    indieMmp,
    indieBooster,
    indieScriptBuff,
    incEffectHPPotion,
    indieAcc,
    indieEva,
    indieAllSkill,
    indieMhpR,
    indieMmpR,
    indieStance,
    indieForceSpeed,
    indieForceJump,
    indieQrPointTerm,
    indieWaterSmashBuff,
    padRate,
    madRate,
    pddRate,
    mddRate,
    accRate,
    evaRate,
    speedRate,
    mhpR,
    mhpRRate,
    mmpR,
    mmpRRate,
    booster,
    expinc,
    str,
    dex,
    inte,
    luk,
    asrR,
    bdR,
    prob,
    party,
    charismaEXP,
    insightEXP,
    willEXP,
    craftEXP,
    senseEXP,
    charmEXP,
    ;
    public static ItemSpecStat getSpecStatByName(String name) {
        if("int".equalsIgnoreCase(name)) {
            return inte;
        }
        return  Arrays.stream(values()).filter(ss ->
                ss.toString().equalsIgnoreCase(name.toLowerCase())).
                findFirst().
                orElse(null);
    }
}
