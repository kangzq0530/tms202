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

package com.msemu.world.enums;

import com.msemu.commons.data.enums.EquipBaseStat;

public enum EnchantStat {

    PAD(0x1),
    MAD(0x2),
    STR(0x4),
    DEX(0x8),
    INT(0x10),
    LUK(0x20),
    PDD(0x40),
    MDD(0x80),
    MHP(0x100),
    MMP(0x200),
    ACC(0x400),
    EVA(0x800),
    JUMP(0x1000),
    SPEED(0x2000),;
    private int val;

    EnchantStat(int val) {
        this.val = val;
    }

    public static EnchantStat getByEquipBaseStat(EquipBaseStat ebs) {
        switch (ebs) {
            case iPAD:
                return PAD;
            case iMAD:
                return MAD;
            case iStr:
                return STR;
            case iDex:
                return DEX;
            case iInt:
                return INT;
            case iLuk:
                return LUK;
            case iPDD:
                return PDD;
            case iMDD:
                return MDD;
            case iMaxHP:
                return MHP;
            case iMaxMP:
                return MMP;
            case iACC:
                return ACC;
            case iEVA:
                return EVA;
            case iJump:
                return JUMP;
            case iSpeed:
                return SPEED;
            default:
                return null;
        }
    }

    public int getValue() {
        return val;
    }

    public EquipBaseStat getEquipBaseStat() {
        switch (this) {
            case PAD:
                return EquipBaseStat.iPAD;
            case MAD:
                return EquipBaseStat.iMAD;
            case STR:
                return EquipBaseStat.iStr;
            case DEX:
                return EquipBaseStat.iDex;
            case INT:
                return EquipBaseStat.iInt;
            case LUK:
                return EquipBaseStat.iLuk;
            case PDD:
                return EquipBaseStat.iPDD;
            case MDD:
                return EquipBaseStat.iMDD;
            case MHP:
                return EquipBaseStat.iMaxHP;
            case MMP:
                return EquipBaseStat.iMaxMP;
            case ACC:
                return EquipBaseStat.iACC;
            case EVA:
                return EquipBaseStat.iEVA;
            case JUMP:
                return EquipBaseStat.iJump;
            case SPEED:
                return EquipBaseStat.iSpeed;
            default:
                return null;
        }
    }
}