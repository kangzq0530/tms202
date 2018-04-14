package com.msemu.world.client.guild;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.character.Character;

import javax.persistence.*;

/**
 * Created by Weber on 2018/4/13.
 */
@Entity
@Table(name = "guildrequestors")
public class GuildRequestor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "charID")
    private int charID;
    @Column(name = "name")
    private String name;
    @Column(name = "job")
    private int job;
    @Column(name = "level")
    private int level;
    @Column(name = "loggedIn")
    private boolean online;

    @Transient
    private Character chr;

    public GuildRequestor() {
    }

    public GuildRequestor(Character chr) {
        this.chr = chr;
        updateInfoFromChar(chr);
    }

    public void updateInfoFromChar(Character chr) {
        setName(chr.getName());
        setCharID(chr.getId());
        setJob(chr.getJob());
        setLevel(chr.getLevel());
        setOnline(chr.isOnline());
    }

    public Character getCharacter() {
        return chr;
    }

    public void setChracter(Character chr) {
        this.chr = chr;
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeString(getName(), 13);
        outPacket.encodeInt(getJob());
        outPacket.encodeInt(getLevel());
        outPacket.encodeInt(0);
        outPacket.encodeInt(isOnline() ? 1 : 0);
        // Following is guild specific info, requestors don't have these
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeLong(0);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GuildMember && ((GuildMember) obj).getCharacter().equals(getCharacter());
    }

    public int getCharID() {
        return charID;
    }

    public void setCharID(int charID) {
        this.charID = charID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }
}
