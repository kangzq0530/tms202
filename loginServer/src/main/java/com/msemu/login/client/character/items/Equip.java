package com.msemu.login.client.character.items;


import com.msemu.commons.data.templates.EquipTemplate;
import com.msemu.commons.database.Schema;
import com.msemu.commons.utils.types.FileTime;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 11/23/2017.
 */
@Schema
@Entity
@Table(name = "Equips")
@PrimaryKeyJoinColumn(name = "itemId")
public class Equip extends Item {
    @Column(name = "serialNumber")
    private long serialNumber;
    @Column(name = "title")
    @Getter
    @Setter
    private String title;
    @JoinColumn(name = "equippedDate")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private FileTime equippedDate = new FileTime();
    @Column(name = "prevBonusExpRate")
    @Getter
    @Setter
    private int prevBonusExpRate;
    @Column(name = "ruc")
    @Getter
    @Setter
    private short ruc;
    @Column(name = "tuc")
    @Getter
    @Setter
    private short tuc;
    @Column(name = "cuc")
    @Getter
    @Setter
    private short cuc;
    @Column(name = "iStr")
    @Getter
    @Setter
    private short iStr;
    @Column(name = "iDex")
    @Getter
    @Setter
    private short iDex;
    @Column(name = "iInt")
    @Getter
    @Setter
    private short iInt;
    @Column(name = "iLuk")
    @Getter
    @Setter
    private short iLuk;
    @Column(name = "iMaxHp")
    @Getter
    @Setter
    private short iMaxHp;
    @Column(name = "iMaxMp")
    @Getter
    @Setter
    private short iMaxMp;
    @Column(name = "iPad")
    @Getter
    @Setter
    private short iPad;
    @Column(name = "iMad")
    @Getter
    @Setter
    private short iMad;
    @Column(name = "iPDD")
    @Getter
    @Setter
    private short iPDD;
    @Column(name = "iMDD")
    @Getter
    @Setter
    private short iMDD;
    @Column(name = "iAcc")
    @Getter
    @Setter
    private short iAcc;
    @Column(name = "iEva")
    @Getter
    @Setter
    private short iEva;
    @Column(name = "iCraft")
    @Getter
    @Setter
    private short iCraft;
    @Column(name = "iSpeed")
    @Getter
    @Setter
    private short iSpeed;
    @Column(name = "iJump")
    @Getter
    @Setter
    private short iJump;
    @Column(name = "attribute")
    @Getter
    @Setter
    private short attribute;
    @Column(name = "levelUpType")
    @Getter
    @Setter
    private short levelUpType;
    @Column(name = "level")
    @Getter
    @Setter
    private short level;
    @Column(name = "exp")
    @Getter
    @Setter
    private short exp;
    @Column(name = "durability")
    @Getter
    @Setter
    private short durability;
    @Column(name = "iuc")
    @Getter
    @Setter
    private short iuc;
    @Column(name = "iPvpDamage")
    @Getter
    @Setter
    private short iPvpDamage;
    @Column(name = "iReduceReq")
    @Getter
    @Setter
    private short iReduceReq;
    @Column(name = "specialAttribute")
    @Getter
    @Setter
    private short specialAttribute;
    @Column(name = "durabilityMax")
    @Getter
    @Setter
    private short durabilityMax;
    @Column(name = "iIncReq")
    @Getter
    @Setter
    private short iIncReq;
    @Column(name = "growthEnchant")
    @Getter
    @Setter
    private short growthEnchant;
    @Column(name = "psEnchant")
    @Getter
    @Setter
    private short psEnchant;
    @Column(name = "bdr")
    @Getter
    @Setter
    private short bdr;
    @Column(name = "imdr")
    @Getter
    @Setter
    private short imdr;
    @Column(name = "damR")
    @Getter
    @Setter
    private short damR;
    @Column(name = "statR")
    @Getter
    @Setter
    private short statR;
    @Column(name = "cuttable")
    @Getter
    @Setter
    private short cuttable;
    @Column(name = "exGradeOption")
    @Getter
    @Setter
    private short exGradeOption;
    @Column(name = "itemState")
    @Getter
    @Setter
    private short itemState;
    @Column(name = "chuc")
    @Getter
    @Setter
    private short chuc;
    @Column(name = "soulOptionId")
    @Getter
    @Setter
    private short soulOptionId;
    @Column(name = "soulSocketId")
    @Getter
    @Setter
    private short soulSocketId;
    @Column(name = "soulOption")
    @Getter
    @Setter
    private short soulOption;
    @Column(name = "rStr")
    @Getter
    @Setter
    private short rStr;
    @Column(name = "rDex")
    @Getter
    @Setter
    private short rDex;
    @Column(name = "rInt")
    @Getter
    @Setter
    private short rInt;
    @Column(name = "rLuk")
    @Getter
    @Setter
    private short rLuk;
    @Column(name = "rLevel")
    @Getter
    @Setter
    private short rLevel;
    @Column(name = "rJob")
    @Getter
    @Setter
    private short rJob;
    @Column(name = "rPop")
    @Getter
    @Setter
    private short rPop;
    @ElementCollection
    @CollectionTable(name = "options", joinColumns = @JoinColumn(name = "equipId"))
    @Column(name = "optionId")
    @Getter
    @Setter
    private List<Integer> options = new ArrayList<>(); // base + add pot
    @Column(name = "specialGrade")
    @Getter
    @Setter
    private int specialGrade;
    @Column(name = "fixedPotential")
    @Getter
    @Setter
    private boolean fixedPotential;
    @Column(name = "tradeBlock")
    @Getter
    @Setter
    private boolean tradeBlock;
    @Column(name = "isOnly")
    @Getter
    @Setter
    private boolean only;
    @Column(name = "notSale")
    @Getter
    @Setter
    private boolean notSale;
    @Column(name = "attackSpeed")
    @Getter
    @Setter
    private int attackSpeed;
    @Column(name = "price")
    @Getter
    @Setter
    private int price;
    @Column(name = "charmEXP")
    @Getter
    @Setter
    private int charmEXP;
    @Column(name = "expireOnLogout")
    @Getter
    @Setter
    private boolean expireOnLogout;
    @Column(name = "setItemID")
    @Getter
    @Setter
    private int setItemID;
    @Column(name = "exItem")
    @Getter
    @Setter
    private boolean exItem;
    @Column(name = "equipTradeBlock")
    @Getter
    @Setter
    private boolean equipTradeBlock;
    @Column(name = "iSlot")
    @Getter
    @Setter
    private String iSlot;
    @Column(name = "vSlot")
    @Getter
    @Setter
    private String vSlot;
    @Column(name = "fixedGrade")
    @Getter
    @Setter
    private int fixedGrade;

    @Getter
    @Setter
    @Transient
    private List<Short> sockets = new ArrayList<>();

    @Getter
    @Setter
    @Transient
    private int socketState;

    public Equip() {
        super();
    }

    public Equip(EquipTemplate t) {
        this.itemId = t.getItemId();
        this.title = t.getTitle();
        // TODO 時間暫時永久
        this.equippedDate = FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME);
        this.prevBonusExpRate = t.getPrevBonusExpRate();
        this.ruc = t.getTuc();
        this.cuc = t.getCuc();
        this.iStr = t.getIStr();
        this.iDex = t.getIDex();
        this.iInt = t.getIInt();
        this.iLuk = t.getILuk();
        this.iMaxHp = t.getIMaxHp();
        this.iMaxMp = t.getIMaxMp();
        this.iPad = t.getIPad();
        this.iMad = t.getIMad();
        this.iAcc = t.getIAcc();
        this.iPDD = t.getIPDD();
        this.iMDD = t.getIMDD();
        this.iEva = t.getIEva();
        this.iCraft = t.getICraft();
        this.iSpeed = t.getISpeed();
        this.iJump = t.getIJump();
        this.iPvpDamage = t.getIPvpDamage();
        this.iReduceReq = t.getIReduceReq();
        this.durabilityMax = t.getDurabilityMax();
        this.iIncReq = t.getIIncReq();
        this.bdr = t.getBdr();
        this.imdr = t.getImdr();
        this.damR = t.getDamR();
        this.statR = t.getStatR();

        this.attribute = 0;
        this.levelUpType = 0;
        this.level = 0;
        this.exp = 0;
        this.durability = 0;
        this.iuc = 0;
        this.specialAttribute = 0;
        this.growthEnchant = 0;
        this.psEnchant = 0;

        this.cuttable = t.getCuttable();
        this.exGradeOption = t.getExGradeOption();
        this.itemState = t.getItemState();
        this.chuc = t.getChuc();
        this.soulOptionId = t.getSoulOptionId();
        this.soulSocketId = t.getSoulSocketId();
        this.soulOption = t.getSoulOption();
        this.rStr = t.getRStr();
        this.rDex = t.getRDex();
        this.rInt = t.getRInt();
        this.rLuk = t.getRLuk();
        this.rLevel = t.getRLevel();
        this.rJob = t.getRJob();
        this.rPop = t.getRPop();
        this.iSlot = t.getISlot();
        this.vSlot = t.getVSlot();
        this.fixedGrade = t.getFixedGrade();
        this.options = new ArrayList<>();
        t.getOptions().values().forEach(option -> {
            this.options.add(option.getOption());
        });
        while (this.options.size() < 7) {
            this.options.add(0);
        }
        this.specialGrade = t.getSpecialGrade();
        this.fixedPotential = t.isFixedPotential();
        this.tradeBlock = t.isTradeBlock();

        this.only = t.isOnly();
        this.notSale = t.isNotSale();
        this.attackSpeed = t.getAttackSpeed();
        this.price = t.getPrice();
        this.charmEXP = t.getCharmEXP();
        this.expireOnLogout = t.isExpireOnLogout();
        this.setItemID = t.getSetItemID();
        this.exItem = t.isExItem();
        this.equipTradeBlock = t.isEquipTradeBlock();
        this.setOwner(getOwner());
        this.itemId = t.getItemId();
        this.cashItemSerialNumber = t.getSerialNumber();
        this.dateExpire = FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME);
        this.invType = t.getInvType();
        this.type = Type.EQUIP;
        this.isCash = t.isCash();

    }

    public Equip deepCopy() {
        Equip ret = new Equip();
        ret.serialNumber = serialNumber;
        ret.title = title;
        ret.equippedDate = equippedDate.deepCopy();
        ret.prevBonusExpRate = prevBonusExpRate;
        ret.ruc = ruc;
        ret.cuc = cuc;
        ret.iStr = iStr;
        ret.iDex = iDex;
        ret.iInt = iInt;
        ret.iLuk = iLuk;
        ret.iMaxHp = iMaxHp;
        ret.iMaxMp = iMaxMp;
        ret.iPad = iPad;
        ret.iMad = iMad;
        ret.iPDD = iPDD;
        ret.iMDD = iMDD;
        ret.iAcc = iAcc;
        ret.iEva = iEva;
        ret.iCraft = iCraft;
        ret.iSpeed = iSpeed;
        ret.iJump = iJump;
        ret.attribute = attribute;
        ret.levelUpType = levelUpType;
        ret.level = level;
        ret.exp = exp;
        ret.durability = durability;
        ret.iuc = iuc;
        ret.iPvpDamage = iPvpDamage;
        ret.iReduceReq = iReduceReq;
        ret.specialAttribute = specialAttribute;
        ret.durabilityMax = durabilityMax;
        ret.iIncReq = iIncReq;
        ret.growthEnchant = growthEnchant;
        ret.psEnchant = psEnchant;
        ret.bdr = bdr;
        ret.imdr = imdr;
        ret.damR = damR;
        ret.statR = statR;
        ret.cuttable = cuttable;
        ret.exGradeOption = exGradeOption;
        ret.itemState = itemState;
        ret.chuc = chuc;
        ret.soulOptionId = soulOptionId;
        ret.soulSocketId = soulSocketId;
        ret.soulOption = soulOption;
        ret.rStr = rStr;
        ret.rDex = rDex;
        ret.rInt = rInt;
        ret.rLuk = rLuk;
        ret.rLevel = rLevel;
        ret.rJob = rJob;
        ret.rPop = rPop;
        ret.iSlot = iSlot;
        ret.vSlot = vSlot;
        ret.fixedGrade = fixedGrade;
        ret.options = new ArrayList<>();
        ret.options.addAll(options);
        ret.specialGrade = specialGrade;
        ret.fixedPotential = fixedPotential;
        ret.tradeBlock = tradeBlock;
        ret.only = only;
        ret.notSale = notSale;
        ret.attackSpeed = attackSpeed;
        ret.price = price;
        ret.charmEXP = charmEXP;
        ret.expireOnLogout = expireOnLogout;
        ret.setItemID = setItemID;
        ret.exItem = exItem;
        ret.equipTradeBlock = equipTradeBlock;
        ret.setOwner(getOwner());
        ret.itemId = itemId;
        ret.cashItemSerialNumber = cashItemSerialNumber;
        ret.dateExpire = dateExpire.deepCopy();
        ret.invType = invType;
        ret.type = type;
        ret.isCash = isCash;
        return ret;
    }
}

