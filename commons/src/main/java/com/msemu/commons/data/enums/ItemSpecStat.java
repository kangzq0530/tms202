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

package com.msemu.commons.data.enums;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/13.
 */
public enum ItemSpecStat {
    hp,
    mp,
    mhp_temp,
    mmp_temp,
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
    indieJump,
    indieExp,
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
    incPVPDamage,
    indieAcc,
    indieEva,
    indieAllSkill,
    indieMhpR,
    indieMmpR,
    indieStance,
    indieForceSpeed,
    indieForceJump,
    //indieQrPointTerm,
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
    expBuff,
    berserk,
    itemupbyitem,
    mesoupbyitem,
    thaw,
    morph,
    darkness,
    weakness,
    seal,
    immortal,
    poison,
    curse,
    incEffectMPPotion,
    preventslip,
    incSoulMP,
    incFixedDamageR,
    inflation,
    ignoreMobpdpR,;

    public static ItemSpecStat getSpecStatByName(String name) {
        if ("int".equalsIgnoreCase(name)) {
            return inte;
        }
        return Arrays.stream(values()).filter(ss ->
                ss.toString().equalsIgnoreCase(name.toLowerCase())).
                findFirst().
                orElse(null);
    }
}
