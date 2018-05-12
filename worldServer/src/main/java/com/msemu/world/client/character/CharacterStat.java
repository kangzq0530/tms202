package com.msemu.world.client.character;

import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.constants.JobConstants;
import com.msemu.world.constants.MapleJob;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Weber on 2018/3/29.
 */
@Schema
@Entity
@Table(name = "characterStats")
@Getter
@Setter
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
        // TODO fill in default vals
    }

    public CharacterStat(String name, int job) {
        this();
        this.name = name;
        this.job = job;
    }

    public void encode(OutPacket<GameClient> outPacket) {
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

