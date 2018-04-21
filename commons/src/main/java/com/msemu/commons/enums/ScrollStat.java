package com.msemu.commons.enums;

import java.util.Arrays;


/**
 * Created by Weber on 2018/4/13.
 */
public enum ScrollStat {
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

    public static ScrollStat getScrollStatByString(String name) {
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
