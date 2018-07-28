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
public enum ItemScrollStat {
    success,
    incSTR,
    incDEX,
    incINT,
    incLUK,
    incPAD,
    incMAD,
    incPDD,
    incMDD,
    incACC,
    incEVA,
    incMHP,
    incMMP,
    incSpeed,
    incJump,
    incIUC,
    incPERIOD,
    incReqLevel,
    reqRUC,
    randOption,
    randStat,
    tuc,
    speed,
    forceUpgrade,
    cursed,
    maxSuperiorEqp,
    noNegative;

    public static ItemScrollStat getScrollStatByString(String name) {
        return Arrays.stream(values()).filter(ss -> ss.toString().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static EquipBaseStat[] getRandStats() {
        return new EquipBaseStat[]{EquipBaseStat.iStr, EquipBaseStat.iDex, EquipBaseStat.iInt, EquipBaseStat.iLuk, EquipBaseStat.iACC, EquipBaseStat.iEVA, EquipBaseStat.iPAD, EquipBaseStat.iMAD, EquipBaseStat.iPDD, EquipBaseStat.iMDD};
    }

    public EquipBaseStat getEquipStat() {
        switch (this) {
            case incSTR:
                return EquipBaseStat.iStr;
            case incDEX:
                return EquipBaseStat.iDex;
            case incINT:
                return EquipBaseStat.iInt;
            case incLUK:
                return EquipBaseStat.iLuk;
            case incPAD:
                return EquipBaseStat.iPAD;
            case incMAD:
                return EquipBaseStat.iMAD;
            case incPDD:
                return EquipBaseStat.iPDD;
            case incMDD:
                return EquipBaseStat.iMDD;
            case incACC:
                return EquipBaseStat.iACC;
            case incEVA:
                return EquipBaseStat.iEVA;
            case incMHP:
                return EquipBaseStat.iMaxHP;
            case incMMP:
                return EquipBaseStat.iMaxMP;
            case incSpeed:
            case speed:
                return EquipBaseStat.iSpeed;
            case incJump:
                return EquipBaseStat.iJump;
            case incReqLevel:
                return EquipBaseStat.iReduceReq;
            default:
                return null;
        }
    }
}
