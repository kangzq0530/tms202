package com.msemu.world.data.templates;

import com.msemu.commons.utils.types.FileTime;
import com.msemu.world.client.character.items.Equip;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private short ruc;
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
    private short growthEnchant;
    @Getter
    @Setter
    private short psEnchant;
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
    private List<Integer> options = new ArrayList<>(); // base + add pot
    @Getter
    @Setter
    private int specialGrade;
    @Getter
    @Setter
    private boolean fixedPotential;
    @Getter
    @Setter
    private boolean tradeBlock;
    @Getter
    @Setter
    private boolean only;
    @Getter
    @Setter
    private boolean notSale;
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
    private boolean expireOnLogout;
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


    public Equip toEquip() {

        Equip ret = new Equip();
        ret.setSerialNumber(getSerialNumber());
        ret.setTitle(getTitle());
        ret.setEquippedDate(new FileTime(-1));
        ret.setPrevBonusExpRate(getPrevBonusExpRate());
        ret.setRuc(getRuc());
        ret.setCuc(getCuc());
        ret.setiStr(getIStr());
        ret.setiDex(getIDex());
        ret.setiLuk(getILuk());
        ret.setiInt(getIInt());
        ret.setiMaxHp(getIMaxHp());
        ret.setiMaxMp(getIMaxMp());
        ret.setiPad(getIPad());
        ret.setiMad(getIMad());
        ret.setiPDD(getIPDD());
        ret.setiMDD(getIMDD());
        ret.setiAcc(getIAcc());
        ret.setiEva(getIEva());
        ret.setiCraft(getICraft());
        ret.setiSpeed(getISpeed());
//        ret.setiJump(getIJump());
//ret.setAttribute(getAttribute());
//ret.setLevelUpType(getLevelUpType());
//        ret.setExp(getExp());
//        ret.levelUpType = levelUpType;
//        ret.level = level;
//        ret.exp = exp;
//        ret.durability = durability;
//        ret.iuc = iuc;
//        ret.iPvpDamage = iPvpDamage;
//        ret.iReduceReq = iReduceReq;
//        ret.specialAttribute = specialAttribute;
//        ret.durabilityMax = durabilityMax;
//        ret.iIncReq = iIncReq;
//        ret.growthEnchant = growthEnchant;
//        ret.psEnchant = psEnchant;
//        ret.bdr = bdr;
//        ret.imdr = imdr;
//        ret.damR = damR;
//        ret.statR = statR;
//        ret.cuttable = cuttable;
//        ret.exGradeOption = exGradeOption;
//        ret.itemState = itemState;
//        ret.chuc = chuc;
//        ret.soulOptionId = soulOptionId;
//        ret.soulSocketId = soulSocketId;
//        ret.soulOption = soulOption;
//        ret.rStr = rStr;
//        ret.rDex = rDex;
//        ret.rInt = rInt;
//        ret.rLuk = rLuk;
//        ret.rLevel = rLevel;
//        ret.rJob = rJob;
//        ret.rPop = rPop;
//        ret.iSlot = iSlot;
//        ret.vSlot = vSlot;
//        ret.fixedGrade = fixedGrade;
//        ret.options = new ArrayList<>();
//        ret.options.addAll(options);
//        ret.specialGrade = specialGrade;
//        ret.fixedPotential = fixedPotential;
//        ret.tradeBlock = tradeBlock;
//        ret.only = only;
//        ret.notSale = notSale;
//        ret.attackSpeed = attackSpeed;
//        ret.price = price;
//        ret.charmEXP = charmEXP;
//        ret.expireOnLogout = expireOnLogout;
//        ret.setItemID = setItemID;
//        ret.exItem = exItem;
//        ret.equipTradeBlock = equipTradeBlock;
//        ret.setOwner("");
//        ret.setItemId(getItemId());
//        ret.setCashItemSerialNumber(-1);
//        ret.setEquippedDate();
//        ret.dateExpire = dateExpire.deepCopy();
//        ret.invType = invType;
//        ret.type = type;
//        ret.isCash = isCash;
        return ret;

    }
}
