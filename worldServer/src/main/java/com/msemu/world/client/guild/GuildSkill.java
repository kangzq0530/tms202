package com.msemu.world.client.guild;

import com.msemu.commons.utils.types.FileTime;
import com.msemu.commons.network.packets.OutPacket;

import javax.persistence.*;

/**
 * Created by Weber on 2018/4/13.
 */
@Entity
public class GuildSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int skillID;
    private short level;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "expireDate")
    private FileTime expireDate;
    private String buyCharacterName;
    private String extendCharacterName;

    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public FileTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(FileTime expireDate) {
        this.expireDate = expireDate;
    }

    public String getBuyCharacterName() {
        return buyCharacterName;
    }

    public void setBuyCharacterName(String buyCharacterName) {
        this.buyCharacterName = buyCharacterName;
    }

    public String getExtendCharacterName() {
        return extendCharacterName;
    }

    public void setExtendCharacterName(String extendCharacterName) {
        this.extendCharacterName = extendCharacterName;
    }

    public void encode(OutPacket outPacket) {
        // GUILDDATA::SKILLENTRY::Decode
        outPacket.encodeShort(getLevel());
        outPacket.encodeFT(getExpireDate());
        outPacket.encodeString(getBuyCharacterName());
        outPacket.encodeString(getExtendCharacterName());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
