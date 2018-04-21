package com.msemu.commons.data.loader.templates;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/4/20.
 */
public class EquipTemplate extends ItemTemplate {

    @Getter
    @Setter
    private long serialNumber;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private int prevBonusExpRate;
    @Getter
    @Setter
    private short tuc;
    @Getter
    @Setter
    private short cuc;
    @Getter
    @Setter
    private short iStr;
    @Getter
    @Setter
    private short iDex;
    @Getter
    @Setter
    private short iInt;
    @Getter
    @Setter
    private short iLuk;
    @Getter
    @Setter
    private short iMaxHp;
    @Getter
    @Setter
    private short iMaxMp;
    @Getter
    @Setter
    private short iPad;
    @Getter
    @Setter
    private short iMad;
    @Getter
    @Setter
    private short iPDD;
    @Getter
    @Setter
    private short iMDD;
    @Getter
    @Setter
    private short iAcc;
    @Getter
    @Setter
    private short iEva;
    @Getter
    @Setter
    private short iCraft;
    @Getter
    @Setter
    private short iSpeed;
    @Getter
    @Setter
    private short iJump;
    @Getter
    @Setter
    private short iuc;
    @Getter
    @Setter
    private short iPvpDamage;
    @Getter
    @Setter
    private short iReduceReq;
    @Getter
    @Setter
    private short specialAttribute;
    @Getter
    @Setter
    private short durabilityMax;
    @Getter
    @Setter
    private short iIncReq;
    @Getter
    @Setter
    private short bdr;
    @Getter
    @Setter
    private short imdr;
    @Getter
    @Setter
    private short damR;
    @Getter
    @Setter
    private short statR;
    @Getter
    @Setter
    private short cuttable;
    @Getter
    @Setter
    private short exGradeOption;
    @Getter
    @Setter
    private short itemState;
    @Getter
    @Setter
    private short chuc;
    @Getter
    @Setter
    private short soulOptionId;
    @Getter
    @Setter
    private short soulSocketId;
    @Getter
    @Setter
    private short soulOption;
    @Getter
    @Setter
    private short rStr;
    @Getter
    @Setter
    private short rDex;
    @Getter
    @Setter
    private short rInt;
    @Getter
    @Setter
    private short rLuk;
    @Getter
    @Setter
    private short rLevel;
    @Getter
    @Setter
    private short rJob;
    @Getter
    @Setter
    private short rPop;
    @Getter
    @Setter
    private Map<Integer, EquipOption> options = new HashMap<>(7); // base + add pot
    @Getter
    @Setter
    private int specialGrade;
    @Getter
    @Setter
    private boolean fixedPotential;
    @Getter
    @Setter
    private boolean only;
    @Getter
    @Setter
    private int attackSpeed;
    @Getter
    @Setter
    private int price;
    @Getter
    @Setter
    private int charmEXP;
    @Getter
    @Setter
    private int setItemID;
    @Getter
    @Setter
    private boolean exItem;
    @Getter
    @Setter
    private boolean equipTradeBlock;
    @Getter
    @Setter
    private String iSlot;
    @Getter
    @Setter
    private String vSlot;
    @Getter
    @Setter
    private int fixedGrade;

    public EquipTemplate() {

    }
}
