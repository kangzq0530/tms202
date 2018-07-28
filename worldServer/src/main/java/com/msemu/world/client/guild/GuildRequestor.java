/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
