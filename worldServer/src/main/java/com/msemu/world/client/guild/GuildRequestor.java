package com.msemu.world.client.guild;

import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Weber on 2018/4/13.
 */
@Schema
@Entity
@Table(name = "guildrequestors")
public class GuildRequestor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;

    @Column(name = "charID")
    @Getter
    @Setter
    private int charID;

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


    @Getter
    @Setter
    @Transient
    private Character character;

    public GuildRequestor() {
    }

    public GuildRequestor(Character character) {
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

}
