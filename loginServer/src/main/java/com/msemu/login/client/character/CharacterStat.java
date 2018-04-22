package com.msemu.login.client.character;

import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.login.constants.JobConstants;
import com.msemu.login.constants.MapleJob;
import lombok.Getter;
import lombok.Setter;

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
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    @Column(name = "characterId")
    private int characterId;
    @Getter
    @Setter
    @Column(name = "characterIdForLog")
    private int characterIdForLog;
    @Getter
    @Setter
    @Column(name = "worldIdForLog")
    private int worldIdForLog;
    @Getter
    @Setter
    @Column(name = "name")
    private String name;
    @Getter
    @Setter
    @Column(name = "gender")
    private int gender;
    @Getter
    @Setter
    @Column(name = "skin")
    private int skin;
    @Getter
    @Setter
    @Column(name = "face")
    private int face;
    @Getter
    @Setter
    @Column(name = "hair")
    private int hair;
    @Getter
    @Setter
    @Column(name = "mixBaseHairColor")
    private int mixBaseHairColor;
    @Getter
    @Setter
    @Column(name = "mixAddHairColor")
    private int mixAddHairColor;
    @Getter
    @Setter
    @Column(name = "mixHairBaseProb")
    private int mixHairBaseProb;
    @Getter
    @Setter
    @Column(name = "maxLevel")
    private int level;
    @Getter
    @Setter
    @Column(name = "job")
    private int job;
    @Getter
    @Setter
    @Column(name = "str")
    private int str;
    @Getter
    @Setter
    @Column(name = "dex")
    private int dex;
    @Getter
    @Setter
    @Column(name = "inte")
    private int inte;
    @Getter
    @Setter
    @Column(name = "luk")
    private int luk;
    @Getter
    @Setter
    @Column(name = "hp")
    private int hp;
    @Getter
    @Setter
    @Column(name = "maxHp")
    private int maxHp;
    @Getter
    @Setter
    @Column(name = "mp")
    private int mp;
    @Getter
    @Setter
    @Column(name = "maxMp")
    private int maxMp;
    @Getter
    @Setter
    @Column(name = "ap")
    private int ap;
    @Getter
    @Setter
    @Column(name = "sp")
    private int sp;
    @Getter
    @Setter
    @Column(name = "exp")
    private long exp;
    @Getter
    @Setter
    @Column(name = "pop")
    private int pop; // fame
    @Getter
    @Setter
    @Column(name = "money")
    private long money;
    @Getter
    @Setter
    @Column(name = "wp")
    private int wp;
    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "extendSP")
    private ExtendSP extendSP;
    @Getter
    @Setter
    @Column(name = "posMap")
    private long posMap;
    @Getter
    @Setter
    @Column(name = "portal")
    private int portal;
    @Getter
    @Setter
    @Column(name = "subJob")
    private int subJob;
    @Getter
    @Setter
    @Column(name = "defFaceAcc")
    private int defFaceAcc;
    @Getter
    @Setter
    @Column(name = "fatigue")
    private int fatigue;
    @Getter
    @Setter
    @Column(name = "lastFatigueUpdateTime")
    private int lastFatigueUpdateTime;
    @Getter
    @Setter
    @Column(name = "charismaExp")
    private int charismaExp;
    @Getter
    @Setter
    @Column(name = "insightExp")
    private int insightExp;
    @Getter
    @Setter
    @Column(name = "willExp")
    private int willExp;
    @Getter
    @Setter
    @Column(name = "craftExp")
    private int craftExp;
    @Getter
    @Setter
    @Column(name = "senseExp")
    private int senseExp;
    @Getter
    @Setter
    @Column(name = "charmExp")
    private int charmExp;
    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nonCombatStatDayLimit")
    private NonCombatStatDayLimit nonCombatStatDayLimit;
    @Getter
    @Setter
    @Column(name = "pvpExp")
    private int pvpExp;
    @Getter
    @Setter
    @Column(name = "pvpGrade")
    private int pvpGrade;
    @Getter
    @Setter
    @Column(name = "pvpPoint")
    private int pvpPoint;
    @Getter
    @Setter
    @Column(name = "pvpModeLevel")
    private int pvpModeLevel;
    @Getter
    @Setter
    @Column(name = "pvpModeType")
    private int pvpModeType;
    @Getter
    @Setter
    @Column(name = "eventPoint")
    private int eventPoint;
    @Getter
    @Setter
    @Column(name = "albaActivityID")
    private int albaActivityID;
    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "albaStartTime")
    private FileTime albaStartTime;
    @Getter
    @Setter
    @Column(name = "albaDuration")
    private int albaDuration;
    @Getter
    @Setter
    @Column(name = "albaSpecialReward")
    private int albaSpecialReward;
    @Getter
    @Setter
    @Column(name = "burning")
    private boolean burning;
    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "characterCard")
    private CharacterCard characterCard;
    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "accountLastLogout")
    private SystemTime accountLastLogout;
    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lastLogout")
    private FileTime lastLogout;
    @Getter
    @Setter
    @Column(name = "gachExp")
    private int gachExp;


    public CharacterStat(String name, int job) {
        this();
        this.name = name;
        this.job = job;
    }

    public CharacterStat() {
        extendSP = new ExtendSP(5);
        nonCombatStatDayLimit = new NonCombatStatDayLimit();
        albaStartTime = new FileTime(0);
        lastLogout = new FileTime(0);
        characterCard = new CharacterCard(0, 0, (byte) 0);
        accountLastLogout = new SystemTime(1970, 1);
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getCharacterId());
        outPacket.encodeInt(getCharacterIdForLog());
        outPacket.encodeInt(getWorldIdForLog());
        outPacket.encodeString(getName(), 15);
        outPacket.encodeByte(getGender());
        outPacket.encodeByte(0); // unk
        outPacket.encodeByte(getSkin());
        outPacket.encodeInt(getFace());
        outPacket.encodeInt(getHair());
        outPacket.encodeByte(-1/*getMixBaseHairColor()*/);
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
        outPacket.encodeByte(7);
        /* Fuck that, setting the above byte lower than 2 will make all 3rd and 4th job that have the property
         ((skillID % 10000) / 10000 == 0) be bugged (you see the maxLevel, but can't actually use it). ?????????????*/
        //outPacket.encodeByte(getPvpModeType());
        outPacket.encodeInt(getEventPoint());
//        outPacket.encodeByte(getAlbaActivityID()); // part time job
//        getAlbaStartTime().encode(outPacket); // long
//        outPacket.encodeInt(getAlbaDuration());
//        outPacket.encodeByte(getAlbaSpecialReward());
        outPacket.encodeInt(0);
        getCharacterCard().encode(outPacket);
        getLastLogout().encodeR(outPacket);
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
