package com.msemu.login.client.character;

import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Weber on 2018/4/14.
 */
@Schema
@Entity
@Table(name = "characterCards")
public class CharacterCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private int id;
    @Column(name = "characterId")
    @Getter
    @Setter
    private int characterId;
    @Column(name = "job")
    @Getter
    @Setter
    private int job;
    @Column(name = "maxLevel")
    @Getter
    @Setter
    private byte level;

    public CharacterCard(){}

    public CharacterCard(int characterId, int job, byte level) {
        this.characterId = characterId;
        this.job = job;
        this.level = level;
    }

    public void encode(OutPacket outPacket) {
        //CHARACTERCARD::Decode
        for(int i = 0; i < 9; i++) {
            outPacket.encodeInt(getCharacterId());
            outPacket.encodeByte(getLevel());
            outPacket.encodeInt(getJob());
        }
    }

}

