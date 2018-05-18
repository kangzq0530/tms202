package com.msemu.world.client.guild;

/**
 * Created by Weber on 2018/4/13.
 */


import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Schema
@Entity
@Table(name = "guildmembers")
public class GuildMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;
    @Column(name = "charID")
    @Getter
    @Setter
    private int charID;
    @Column(name = "grade")
    @Getter
    @Setter
    private int grade;
    @Column(name = "allianceGrade")
    @Getter
    @Setter
    private int allianceGrade;
    @Column(name = "commitment")
    @Getter
    @Setter
    private int commitment;
    @Column(name = "dayCommitment")
    @Getter
    @Setter
    private int dayCommitment;
    @Column(name = "igp")
    @Getter
    @Setter
    private int igp;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "commitmentIncTime")
    @Getter
    @Setter
    private FileTime commitmentIncTime;
    @Column(name = "name")
    @Getter
    @Setter
    private String name;
    @Column(name = "job")
    @Getter
    @Setter
    private int job;
    @Column(name = "level")
    @Getter
    @Setter
    private int level;
    @Column(name = "loggedIn")
    @Getter
    @Setter
    private boolean online;


    @Transient
    @Getter
    @Setter
    private Character character;

    public GuildMember() {
    }

    public GuildMember(Character character) {
        this.character = character;
        updateInfoFromChar(character);
    }

    public void updateInfoFromChar(Character chr) {
        setName(chr.getName());
        setCharID(chr.getId());
        setJob(chr.getJob());
        setLevel(chr.getLevel());
        setOnline(chr.isOnline());
    }

    public void encode(OutPacket<GameClient> outPacket) {
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

}

