package com.msemu.world.client.guild;

/**
 * Created by Weber on 2018/4/13.
 */


import com.msemu.commons.utils.types.FileTime;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.character.Character;

import javax.persistence.*;

@Entity
@Table(name = "guildmembers")
public class GuildMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "charID")
    private int charID;
    @Column(name = "grade")
    private int grade;
    @Column(name = "allianceGrade")
    private int allianceGrade;
    @Column(name = "commitment")
    private int commitment;
    @Column(name = "dayCommitment")
    private int dayCommitment;
    @Column(name = "igp")
    private int igp;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "commitmentIncTime")
    private FileTime commitmentIncTime;
    @Column(name = "name")
    private String name;
    @Column(name = "job")
    private int job;
    @Column(name = "level")
    private int level;
    @Column(name = "loggedIn")
    private boolean online;


    @Transient
    private Character character;

    public GuildMember() {
    }

    public GuildMember(Character chr) {
        this.character = chr;
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
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getAllianceGrade() {
        return allianceGrade;
    }

    public void setAllianceGrade(int allianceGrade) {
        this.allianceGrade = allianceGrade;
    }

    public int getCommitment() {
        return commitment;
    }

    public void setCommitment(int commitment) {
        this.commitment = commitment;
    }

    public int getDayCommitment() {
        return dayCommitment;
    }

    public void setDayCommitment(int dayCommitment) {
        this.dayCommitment = dayCommitment;
    }

    public int getIgp() {
        return igp;
    }

    public void setIgp(int igp) {
        this.igp = igp;
    }

    public FileTime getCommitmentIncTime() {
        return commitmentIncTime;
    }

    public void setCommitmentIncTime(FileTime commitmentIncTime) {
        this.commitmentIncTime = commitmentIncTime;
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeString(getName(), 15);
        outPacket.encodeInt(getJob());
        outPacket.encodeInt(getLevel());
        outPacket.encodeInt(getGrade());
        outPacket.encodeInt(isOnline() ? 1 : 0);
        outPacket.encodeInt(getAllianceGrade());
        outPacket.encodeInt(getCommitment());
        outPacket.encodeInt(getDayCommitment());
        outPacket.encodeInt(getIgp());
        outPacket.encodeFT(getCommitmentIncTime());
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

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}

