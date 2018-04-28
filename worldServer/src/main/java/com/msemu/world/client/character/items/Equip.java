package com.msemu.world.client.character.items;


import com.msemu.commons.data.enums.*;
import com.msemu.commons.data.templates.EquipTemplate;
import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.Rand;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import com.msemu.world.constants.ItemConstants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

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
    @Column(name = "tuc")
    @Getter
    @Setter
    private short ruc;
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
    private List<Integer> options = new ArrayList<>(7); // base + add pot
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
        while(this.options.size() < 7)
            this.options.add(0);
        while(this.sockets.size() < 3)
            this.sockets.add((short) 0);
    }

    public Equip(EquipTemplate t) {
        super(t);
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
        t.getOptions().forEach((index, option) -> this.options.add(option.getOption()));
        while(this.options.size() < 7)
            this.options.add(0);
        while(this.sockets.size() < 3)
            this.sockets.add((short) 0);
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
        ret.options = new ArrayList<>(7);
        ret.options.addAll(options);
        while(this.options.size() < 7)
            this.options.add(0);
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

    public long getSerialNumber() {
        return serialNumber;
    }

    public void addAttribute(EquipAttribute ea) {
        short attr = getAttribute();
        attr |= ea.getValue();
        setAttribute(attr);
    }

    public void addSpecialAttribute(EquipSpecialAttribute esa) {
        short attr = getSpecialAttribute();
        attr |= esa.getValue();
        setSpecialAttribute(attr);
    }


    public short getGrade() {
        ItemGrade ig = ItemGrade.getGradeByVal(Math.min(getBaseGrade(), getBonusGrade()));
        switch (ig) {
            case HIDDEN_RARE:
            case HIDDEN_EPIC:
            case HIDDEN_UNIQUE:
            case HIDDEN_LEGENDARY:
                return ig.getValue();
        }
        return (short) Math.max(getBaseGrade(), getBonusGrade());
    }

    public short getBaseGrade() {
        return ItemGrade.getGradeByOption(getOptionBase(0)).getValue();
    }

    public short getBonusGrade() {
        return ItemGrade.getGradeByOption(getOptionBonus(0)).getValue();
    }

    public void encode(OutPacket<GameClient> outPacket) {
        // GW_ItemSlotBase
        super.encode(outPacket);

        // TODO ???
        outPacket.encodeByte(false);

        int mask0 = getStatMask(0);
        outPacket.encodeInt(mask0);
        if (hasStat(EquipBaseStat.ruc)) {
            outPacket.encodeByte(getRuc());
        }
        if (hasStat(EquipBaseStat.cuc)) {
            outPacket.encodeByte(getCuc());
        }
        if (hasStat(EquipBaseStat.iStr)) {
            outPacket.encodeShort(getIStr());
        }
        if (hasStat(EquipBaseStat.iDex)) {
            outPacket.encodeShort(getIDex());
        }
        if (hasStat(EquipBaseStat.iInt)) {
            outPacket.encodeShort(getIInt());
        }
        if (hasStat(EquipBaseStat.iLuk)) {
            outPacket.encodeShort(getILuk());
        }
        if (hasStat(EquipBaseStat.iMaxHP)) {
            outPacket.encodeShort(getIMaxHp());
        }
        if (hasStat(EquipBaseStat.iMaxMP)) {
            outPacket.encodeShort(getIMaxMp());
        }
        if (hasStat(EquipBaseStat.iPAD)) {
            outPacket.encodeShort(getIPad());
        }
        if (hasStat(EquipBaseStat.iMAD)) {
            outPacket.encodeShort(getIMad());
        }
        if (hasStat(EquipBaseStat.iPDD)) {
            outPacket.encodeShort(getIPDD());
        }
        if (hasStat(EquipBaseStat.iCraft)) {
            outPacket.encodeShort(getICraft());
        }
        if (hasStat(EquipBaseStat.iSpeed)) {
            outPacket.encodeShort(getISpeed());
        }
        if (hasStat(EquipBaseStat.iJump)) {
            outPacket.encodeShort(getIJump());
        }
        if (hasStat(EquipBaseStat.attribute)) {
            outPacket.encodeInt(getAttribute());
        }
        if (hasStat(EquipBaseStat.levelUpType)) {
            outPacket.encodeByte(getLevelUpType());
        }
        if (hasStat(EquipBaseStat.level)) {
            outPacket.encodeByte(getLevel());
        }
        if (hasStat(EquipBaseStat.exp)) {
            outPacket.encodeLong(getExp());
        }
        if (hasStat(EquipBaseStat.durability)) {
            outPacket.encodeInt(getDurability());
        }
        if (hasStat(EquipBaseStat.iuc)) {
            outPacket.encodeInt(getIuc()); // hammer
        }
        if (hasStat(EquipBaseStat.iPvpDamage)) {
            outPacket.encodeShort(getIPvpDamage());
        }
        if (hasStat(EquipBaseStat.iReduceReq)) {
            outPacket.encodeByte(getIReduceReq());
        }
        if (hasStat(EquipBaseStat.specialAttribute)) {
            outPacket.encodeShort(getSpecialAttribute());
        }
        if (hasStat(EquipBaseStat.durabilityMax)) {
            outPacket.encodeInt(getDurabilityMax());
        }
        if (hasStat(EquipBaseStat.iIncReq)) {
            outPacket.encodeByte(getIIncReq());
        }
        if (hasStat(EquipBaseStat.growthEnchant)) {
            outPacket.encodeByte(getGrowthEnchant()); // ygg
        }
        if (hasStat(EquipBaseStat.psEnchant)) {
            outPacket.encodeByte(getPsEnchant()); // final strike
        }
        if (hasStat(EquipBaseStat.bdr)) {
            outPacket.encodeByte(getBdr()); // bd
        }
        if (hasStat(EquipBaseStat.imdr)) {
            outPacket.encodeByte(getImdr()); // ied
        }
        outPacket.encodeInt(getStatMask(1)); // mask 2
        if (hasStat(EquipBaseStat.damR)) {
            outPacket.encodeByte(getDamR()); // td
        }
        if (hasStat(EquipBaseStat.statR)) {
            outPacket.encodeByte(getStatR()); // as
        }
        if (hasStat(EquipBaseStat.cuttable)) {
            outPacket.encodeByte(getCuttable()); // soc
        }
        if (hasStat(EquipBaseStat.exGradeOption)) {
            outPacket.encodeLong(getExGradeOption());
        }
        if (hasStat(EquipBaseStat.itemState)) {
            outPacket.encodeInt(getItemState());
        }
        // GW_ItemSlotEquipOpt
        outPacket.encodeString(getOwner());
        //潛能等級
        outPacket.encodeByte(getGrade());
        //裝備星級
        outPacket.encodeByte(getChuc());
        //潛在能力 * 3 附加潛能 * 3 鐵砧
        for (int i = 0; i < 7; i++) {
            outPacket.encodeShort(getOptions().get(i)); // 7x, last is fusion anvil
        }
        // Alien Stone
        outPacket.encodeShort(getSocketState()); // socket state
        // Alien Stone能力(Item.wz/Install/0306.img) > 0 = 安裝, 0 = 空, -1 = 無.
        for (int i = 0; i < 3; i++) {
            outPacket.encodeShort(/*getSockets().get(i)*/ 0); // sockets 0 through 2 (-1 = none, 0 = empty, >0 = filled
        }
        boolean hasUniqueId = getSerialNumber() > 0 && !ItemConstants.類型.結婚戒指(getItemId()) && getItemId() / 10000 != 166;

        // uniqueId
        if (!hasUniqueId) {
            outPacket.encodeLong(getId()); // ?
        }
        // // ftEquipped
        outPacket.encodeFT(FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME));
        // nPrevBonusExpRate
        outPacket.encodeInt(-1);
        // GW_CashItemOption {
        outPacket.encodeLong(getCashItemSerialNumber());
        // ftExpireDate
        getDateExpire().encode(outPacket);
        outPacket.encodeInt(getGrade());
        // anOption
        for (int i = 0; i < 3; i++) {
            outPacket.encodeInt(0);
        }
        // }
        // 靈魂寶珠
        outPacket.encodeShort(getSoulOptionId());
        // 靈魂捲軸
        outPacket.encodeShort(getSoulSocketId());
        // 靈魂潛能
        outPacket.encodeShort(getSoulOption());
        if (getItemId() / 10000 == 172) {
            outPacket.encodeShort(0);
            outPacket.encodeInt(0);
            outPacket.encodeShort(0);
        }
        // 突破傷害上限
        outPacket.encodeInt(0); //outPacket.encodeInt(ItemConstants.類型.武器(getItemId()) ? ItemConstants.getMaxDamageLimitBreak(getItemId()) : 0;);
        FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME).encodeR(outPacket);
    }

    private boolean hasStat(EquipBaseStat ebs) {
        return getBaseStat(ebs) != 0;
    }

    private int getStatMask(int pos) {
        int mask = 0;
        for (EquipBaseStat ebs : EquipBaseStat.values()) {
            if (getBaseStat(ebs) != 0 && ebs.getPos() == pos) {
                mask |= ebs.getVal();
            }
        }
        return mask;
    }

    public void setBaseStat(EquipBaseStat equipBaseStat, long amount) {
        switch (equipBaseStat) {
            case ruc:
                setRuc((short) amount);
                break;
            case cuc:
                setCuc((short) amount);
                break;
            case iStr:
                setIStr((short) amount);
                break;
            case iDex:
                setIDex((short) amount);
                break;
            case iInt:
                setIInt((short) amount);
                break;
            case iLuk:
                setILuk((short) amount);
                break;
            case iMaxHP:
                setIMaxHp((short) amount);
                break;
            case iMaxMP:
                setIMaxMp((short) amount);
                break;
            case iPAD:
                setIPad((short) amount);
                break;
            case iMAD:
                setIMad((short) amount);
                break;
            case iPDD:
                setIPDD((short) amount);
                break;
            case iMDD:
                setIMDD((short) amount);
                break;
            case iACC:
                setIAcc((short) amount);
                break;
            case iEVA:
                setIEva((short) amount);
                break;
            case iCraft:
                setICraft((short) amount);
                break;
            case iSpeed:
                setISpeed((short) amount);
                break;
            case iJump:
                setIJump((short) amount);
                break;
            case attribute:
                setAttribute((short) amount);
                break;
            case levelUpType:
                setLevelUpType((short) amount);
                break;
            case level:
                setLevel((short) amount);
                break;
            case exp:
                setExp((short) amount);
                break;
            case durability:
                setDurability((short) amount);
                break;
            case iuc:
                setIuc((short) amount);
                break;
            case iPvpDamage:
                setIPvpDamage((short) amount);
                break;
            case iReduceReq:
                setIReduceReq((short) amount);
                break;
            case specialAttribute:
                setSpecialAttribute((short) amount);
                break;
            case durabilityMax:
                setDurabilityMax((short) amount);
                break;
            case iIncReq:
                setIIncReq((short) amount);
                break;
            case growthEnchant:
                setGrowthEnchant((short) amount);
                break;
            case psEnchant:
                setPsEnchant((short) amount);
                break;
            case bdr:
                setBdr((short) amount);
                break;
            case imdr:
                setImdr((short) amount);
                break;
            case damR:
                setDamR((short) amount);
                break;
            case statR:
                setStatR((short) amount);
                break;
            case cuttable:
                setCuttable((short) amount);
                break;
            case exGradeOption:
                setExGradeOption((short) amount);
                break;
            case itemState:
                setItemState((short) amount);
                break;
        }
    }

    public long getBaseStat(EquipBaseStat equipBaseStat) {
        switch (equipBaseStat) {
            case ruc:
                return getRuc();
            case cuc:
                return getCuc();
            case iStr:
                return getIStr();
            case iDex:
                return getIDex();
            case iInt:
                return getIInt();
            case iLuk:
                return getILuk();
            case iMaxHP:
                return getIMaxHp();
            case iMaxMP:
                return getIMaxMp();
            case iPAD:
                return getIPad();
            case iMAD:
                return getIMad();
            case iPDD:
                return getIPDD();
            case iMDD:
                return getIMDD();
            case iACC:
                return getIAcc();
            case iEVA:
                return getIEva();
            case iCraft:
                return getICraft();
            case iSpeed:
                return getISpeed();
            case iJump:
                return getIJump();
            case attribute:
                return getAttribute();
            case levelUpType:
                return getLevelUpType();
            case level:
                return getLevel();
            case exp:
                return getExp();
            case durability:
                return getDurability();
            case iuc:
                return getIuc();
            case iPvpDamage:
                return getIPvpDamage();
            case iReduceReq:
                return getIReduceReq();
            case specialAttribute:
                return getSpecialAttribute();
            case durabilityMax:
                return getDurabilityMax();
            case iIncReq:
                return getIIncReq();
            case growthEnchant:
                return getGrowthEnchant();
            case psEnchant:
                return getPsEnchant();
            case bdr:
                return getBdr();
            case imdr:
                return getImdr();
            case damR:
                return getDamR();
            case statR:
                return getStatR();
            case cuttable:
                return getCuttable();
            case exGradeOption:
                return getExGradeOption();
            case itemState:
                return getItemState();
            default:
                return 0;
        }
    }

    public void addStat(EquipBaseStat stat, int amount) {
        int cur = (int) getBaseStat(stat);
        int newStat = cur + amount >= 0 ? cur + amount : 0; // stat cannot be negative
        setBaseStat(stat, newStat);
    }

    public boolean hasAttribute(EquipAttribute equipAttribute) {
        return (getAttribute() & equipAttribute.getValue()) != 0;
    }

    public boolean hasSpecialAttribute(EquipSpecialAttribute equipSpecialAttribute) {
        return (getSpecialAttribute() & equipSpecialAttribute.getValue()) != 0;
    }

    public void removeAttribute(EquipAttribute equipAttribute) {
        if (!hasAttribute(equipAttribute)) {
            return;
        }
        short attr = getAttribute();
        attr ^= equipAttribute.getValue();
        setAttribute(attr);
    }

    public void removeSpecialAttribute(EquipSpecialAttribute equipSpecialAttribute) {
        if (!hasSpecialAttribute(equipSpecialAttribute)) {
            return;
        }
        short attr = getSpecialAttribute();
        attr ^= equipSpecialAttribute.getValue();
        setSpecialAttribute(attr);
    }

    public TreeMap<EnchantStat, Integer> getHyperUpgradeStats() {
        Comparator<EnchantStat> comparator = Comparator.comparingInt(EnchantStat::getValue);
        TreeMap<EnchantStat, Integer> res = new TreeMap<>(comparator);
        return res;
    }

    public int[] getOptionBase() {
        return new int[]{getOptions().get(0), getOptions().get(1), getOptions().get(2)};
    }

    public int getOptionBase(int num) {
        return getOptions().get(num);
    }

    public int setOptionBase(int num, int val) {
        return getOptions().set(num, val);
    }

    public int[] getOptionBonus() {
        return new int[]{getOptions().get(3), getOptions().get(4), getOptions().get(5)};
    }

    public int getOptionBonus(int num) {
        return getOptions().get(num + 3);
    }

    public int setOptionBonus(int num, int val) {
        return getOptions().set(num + 3, val);
    }

    public int getRandomOption(boolean bonus) {
        // TODO Random OPtion
        return options.get(Rand.get(options.size() - 1));
    }

    public void setHiddenOptionBase(short val, int thirdLineChance) {
        int max = 3;
        if (getOptionBase(3) == 0) {
            // If this equip did not have a 3rd line already, thirdLineChance to get it
            if (Rand.getChance(100 - thirdLineChance)) {
                max = 2;
            }
        }
        for (int i = 0; i < max; i++) {
            setOptionBase(i, -val);
        }
    }

    public void setHiddenOptionBonus(short val, int thirdLineChance) {
        int max = 3;
        if (getOptionBonus(3) == 0) {
            // If this equip did not have a 3rd line already, thirdLineChance to get it
            if (Rand.getChance(100 - thirdLineChance)) {
                max = 2;
            }
        }
        for (int i = 0; i < max; i++) {
            setOptionBonus(i, -val);
        }
    }

    public void releaseOptions(boolean bonus) {
        // 重置潛能
    }
}

