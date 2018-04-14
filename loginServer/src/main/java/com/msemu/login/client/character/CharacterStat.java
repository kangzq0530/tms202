package com.msemu.login.client.character;

import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.login.constants.JobConstants;
import com.msemu.login.constants.MapleJob;

import javax.persistence.*;

/**
 * Created by Weber on 2018/4/14.
 */
@Schema
@Entity
@Table(name = "characterStats")
public class CharacterStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "characterId")
    private int characterId;
    @Column(name = "characterIdForLog")
    private int characterIdForLog;
    @Column(name = "worldIdForLog")
    private int worldIdForLog;
    @Column(name = "name")
    private String name;
    @Column(name = "gender")
    private int gender;
    @Column(name = "skin")
    private int skin;
    @Column(name = "face")
    private int face;
    @Column(name = "hair")
    private int hair;
    @Column(name = "mixBaseHairColor")
    private int mixBaseHairColor;
    @Column(name = "mixAddHairColor")
    private int mixAddHairColor;
    @Column(name = "mixHairBaseProb")
    private int mixHairBaseProb;
    @Column(name = "level")
    private int level;
    @Column(name = "job")
    private int job;
    @Column(name = "str")
    private int str;
    @Column(name = "dex")
    private int dex;
    @Column(name = "inte")
    private int inte;
    @Column(name = "luk")
    private int luk;
    @Column(name = "hp")
    private int hp;
    @Column(name = "maxHp")
    private int maxHp;
    @Column(name = "mp")
    private int mp;
    @Column(name = "maxMp")
    private int maxMp;
    @Column(name = "ap")
    private int ap;
    @Column(name = "sp")
    private int sp;
    @Column(name = "exp")
    private long exp;
    @Column(name = "pop")
    private int pop; // fame
    @Column(name = "money")
    private long money;
    @Column(name = "wp")
    private int wp;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "extendSP")
    private ExtendSP extendSP;
    @Column(name = "posMap")
    private long posMap;
    @Column(name = "portal")
    private int portal;
    @Column(name = "subJob")
    private int subJob;
    @Column(name = "defFaceAcc")
    private int defFaceAcc;
    @Column(name = "fatigue")
    private int fatigue;
    @Column(name = "lastFatigueUpdateTime")
    private int lastFatigueUpdateTime;
    @Column(name = "charismaExp")
    private int charismaExp;
    @Column(name = "insightExp")
    private int insightExp;
    @Column(name = "willExp")
    private int willExp;
    @Column(name = "craftExp")
    private int craftExp;
    @Column(name = "senseExp")
    private int senseExp;
    @Column(name = "charmExp")
    private int charmExp;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nonCombatStatDayLimit")
    private NonCombatStatDayLimit nonCombatStatDayLimit;
    @Column(name = "pvpExp")
    private int pvpExp;
    @Column(name = "pvpGrade")
    private int pvpGrade;
    @Column(name = "pvpPoint")
    private int pvpPoint;
    @Column(name = "pvpModeLevel")
    private int pvpModeLevel;
    @Column(name = "pvpModeType")
    private int pvpModeType;
    @Column(name = "eventPoint")
    private int eventPoint;
    @Column(name = "albaActivityID")
    private int albaActivityID;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "albaStartTime")
    private FileTime albaStartTime;
    @Column(name = "albaDuration")
    private int albaDuration;
    @Column(name = "albaSpecialReward")
    private int albaSpecialReward;
    @Column(name = "burning")
    private boolean burning;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "characterCard")
    private CharacterCard characterCard;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "accountLastLogout")
    private SystemTime accountLastLogout;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lastLogout")
    private FileTime lastLogout;
    @Column(name = "gachExp")
    private int gachExp;

    public CharacterStat() {
        extendSP = new ExtendSP(5);
        nonCombatStatDayLimit = new NonCombatStatDayLimit();
        albaStartTime = new FileTime(0);
        lastLogout = new FileTime(0);
        characterCard = new CharacterCard(0, 0, (byte) 0);
        accountLastLogout = new SystemTime(1970, 1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getCharacterIdForLog() {
        return characterIdForLog;
    }

    public void setCharacterIdForLog(int characterIdForLog) {
        this.characterIdForLog = characterIdForLog;
    }

    public int getWorldIdForLog() {
        return worldIdForLog;
    }

    public void setWorldIdForLog(int worldIdForLog) {
        this.worldIdForLog = worldIdForLog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getSkin() {
        return skin;
    }

    public void setSkin(int skin) {
        this.skin = skin;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public int getHair() {
        return hair;
    }

    public void setHair(int hair) {
        this.hair = hair;
    }

    public int getMixBaseHairColor() {
        return mixBaseHairColor;
    }

    public void setMixBaseHairColor(int mixBaseHairColor) {
        this.mixBaseHairColor = mixBaseHairColor;
    }

    public int getMixAddHairColor() {
        return mixAddHairColor;
    }

    public void setMixAddHairColor(int mixAddHairColor) {
        this.mixAddHairColor = mixAddHairColor;
    }

    public int getMixHairBaseProb() {
        return mixHairBaseProb;
    }

    public void setMixHairBaseProb(int mixHairBaseProb) {
        this.mixHairBaseProb = mixHairBaseProb;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getDex() {
        return dex;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public int getInte() {
        return inte;
    }

    public void setInte(int inte) {
        this.inte = inte;
    }

    public int getLuk() {
        return luk;
    }

    public void setLuk(int luk) {
        this.luk = luk;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(int maxMp) {
        this.maxMp = maxMp;
    }

    public int getAp() {
        return ap;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

    public int getSp() {
        return sp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public int getWp() {
        return wp;
    }

    public void setWp(int wp) {
        this.wp = wp;
    }

    public ExtendSP getExtendSP() {
        return extendSP;
    }

    public void setExtendSP(ExtendSP extendSP) {
        this.extendSP = extendSP;
    }

    public long getPosMap() {
        return posMap;
    }

    public void setPosMap(long posMap) {
        this.posMap = posMap;
    }

    public int getPortal() {
        return portal;
    }

    public void setPortal(int portal) {
        this.portal = portal;
    }

    public int getSubJob() {
        return subJob;
    }

    public void setSubJob(int subJob) {
        this.subJob = subJob;
    }

    public int getDefFaceAcc() {
        return defFaceAcc;
    }

    public void setDefFaceAcc(int defFaceAcc) {
        this.defFaceAcc = defFaceAcc;
    }

    public int getFatigue() {
        return fatigue;
    }

    public void setFatigue(int fatigue) {
        this.fatigue = fatigue;
    }

    public int getLastFatigueUpdateTime() {
        return lastFatigueUpdateTime;
    }

    public void setLastFatigueUpdateTime(int lastFatigueUpdateTime) {
        this.lastFatigueUpdateTime = lastFatigueUpdateTime;
    }

    public int getCharismaExp() {
        return charismaExp;
    }

    public void setCharismaExp(int charismaExp) {
        this.charismaExp = charismaExp;
    }

    public int getInsightExp() {
        return insightExp;
    }

    public void setInsightExp(int insightExp) {
        this.insightExp = insightExp;
    }

    public int getWillExp() {
        return willExp;
    }

    public void setWillExp(int willExp) {
        this.willExp = willExp;
    }

    public int getCraftExp() {
        return craftExp;
    }

    public void setCraftExp(int craftExp) {
        this.craftExp = craftExp;
    }

    public int getSenseExp() {
        return senseExp;
    }

    public void setSenseExp(int senseExp) {
        this.senseExp = senseExp;
    }

    public int getCharmExp() {
        return charmExp;
    }

    public void setCharmExp(int charmExp) {
        this.charmExp = charmExp;
    }

    public NonCombatStatDayLimit getNonCombatStatDayLimit() {
        return nonCombatStatDayLimit;
    }

    public void setNonCombatStatDayLimit(NonCombatStatDayLimit nonCombatStatDayLimit) {
        this.nonCombatStatDayLimit = nonCombatStatDayLimit;
    }

    public int getPvpExp() {
        return pvpExp;
    }

    public void setPvpExp(int pvpExp) {
        this.pvpExp = pvpExp;
    }

    public int getPvpGrade() {
        return pvpGrade;
    }

    public void setPvpGrade(int pvpGrade) {
        this.pvpGrade = pvpGrade;
    }

    public int getPvpPoint() {
        return pvpPoint;
    }

    public void setPvpPoint(int pvpPoint) {
        this.pvpPoint = pvpPoint;
    }

    public int getPvpModeLevel() {
        return pvpModeLevel;
    }

    public void setPvpModeLevel(int pvpModeLevel) {
        this.pvpModeLevel = pvpModeLevel;
    }

    public int getPvpModeType() {
        return pvpModeType;
    }

    public void setPvpModeType(int pvpModeType) {
        this.pvpModeType = pvpModeType;
    }

    public int getEventPoint() {
        return eventPoint;
    }

    public void setEventPoint(int eventPoint) {
        this.eventPoint = eventPoint;
    }

    public int getAlbaActivityID() {
        return albaActivityID;
    }

    public void setAlbaActivityID(int albaActivityID) {
        this.albaActivityID = albaActivityID;
    }

    public FileTime getAlbaStartTime() {
        return albaStartTime;
    }

    public void setAlbaStartTime(FileTime albaStartTime) {
        this.albaStartTime = albaStartTime;
    }

    public int getAlbaDuration() {
        return albaDuration;
    }

    public void setAlbaDuration(int albaDuration) {
        this.albaDuration = albaDuration;
    }

    public int getAlbaSpecialReward() {
        return albaSpecialReward;
    }

    public void setAlbaSpecialReward(int albaSpecialReward) {
        this.albaSpecialReward = albaSpecialReward;
    }

    public boolean isBurning() {
        return burning;
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }

    public CharacterCard getCharacterCard() {
        return characterCard;
    }

    public void setCharacterCard(CharacterCard characterCard) {
        this.characterCard = characterCard;
    }

    public SystemTime getAccountLastLogout() {
        return accountLastLogout;
    }

    public void setAccountLastLogout(SystemTime accountLastLogout) {
        this.accountLastLogout = accountLastLogout;
    }

    public FileTime getLastLogout() {
        return lastLogout;
    }

    public void setLastLogout(FileTime lastLogout) {
        this.lastLogout = lastLogout;
    }

    public int getGachExp() {
        return gachExp;
    }

    public void setGachExp(int gachExp) {
        this.gachExp = gachExp;
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getCharacterId());
        outPacket.encodeInt(getCharacterIdForLog());
        outPacket.encodeInt(getWorldIdForLog());
        outPacket.encodeString(getName(), 13);
        outPacket.encodeByte(getGender());
        outPacket.encodeByte(getSkin());
        outPacket.encodeByte(0); // unk
        outPacket.encodeInt(getFace());
        outPacket.encodeInt(getHair());
        outPacket.encodeByte(getMixBaseHairColor());
        outPacket.encodeByte(getMixAddHairColor());
        outPacket.encodeByte(getMixHairBaseProb());
        outPacket.encodeByte(getLevel());
        outPacket.encodeShort(getJob());
        outPacket.encodeShort(getStr());
        outPacket.encodeShort(getDex());
        outPacket.encodeShort(getInte());
        outPacket.encodeShort(getLuk());
        outPacket.encodeInt(getHp());
        outPacket.encodeInt(getMaxHp());
        outPacket.encodeInt(getMp());
        outPacket.encodeInt(getMaxMp());
        outPacket.encodeShort(getAp());
        if (JobConstants.isSeparatedSp(getJob())) {
            getExtendSP().encode(outPacket);
        } else {
            outPacket.encodeShort(getSp());
        }
        outPacket.encodeLong(getExp());
        outPacket.encodeInt(getPop());
        outPacket.encodeInt(getWp()); // 神之子武器點數
        outPacket.encodeLong(getGachExp());
        outPacket.encodeFT(FileTime.getTime());
        outPacket.encodeInt((int) getPosMap());
        outPacket.encodeByte(getPortal());
        outPacket.encodeShort(getSubJob());
        if (MapleJob.is惡魔(getJob()) || MapleJob.is傑諾(getJob()) || MapleJob.is幻獸師(getJob())) {
            outPacket.encodeInt(getDefFaceAcc());
        }
        outPacket.encodeShort(getFatigue());
        outPacket.encodeInt(getLastFatigueUpdateTime());

        outPacket.encodeInt(getCharismaExp());
        outPacket.encodeInt(getInsightExp());
        outPacket.encodeInt(getWillExp());
        outPacket.encodeInt(getCraftExp());
        outPacket.encodeInt(getSenseExp());
        outPacket.encodeInt(getCharmExp());

        getNonCombatStatDayLimit().encode(outPacket);

        outPacket.encodeInt(getPvpExp());
        outPacket.encodeByte(getPvpGrade());
        outPacket.encodeInt(getPvpPoint());
        outPacket.encodeByte(6);
        /* Fuck that, setting the above byte lower than 2 will make all 3rd and 4th job that have the property
         ((skillID % 10000) / 10000 == 0) be bugged (you see the level, but can't actually use it). ?????????????*/
        outPacket.encodeByte(getPvpModeType());
        outPacket.encodeInt(getEventPoint());
//        outPacket.encodeByte(getAlbaActivityID()); // part time job
//        getAlbaStartTime().encode(outPacket); // long
//        outPacket.encodeInt(getAlbaDuration());
//        outPacket.encodeByte(getAlbaSpecialReward());
        outPacket.encodeInt(0);
        getCharacterCard().encode(outPacket);
        getLastLogout().encode(outPacket);
        //
        outPacket.encodeLong(0);
        outPacket.encodeLong(0);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeByte(0);
        //
        outPacket.encodeZeroBytes(25);
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        outPacket.encodeByte(isBurning()); // bBurning 不確定
    }
}
