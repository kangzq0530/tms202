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

package com.msemu.world.client.character.party;

import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
public class PartyMember {

    @Getter
    private final Character character;

    @Getter
    @Setter
    private int partyLeaderId;

    @Getter
    @Setter
    private TownPortal townPortal;

    public PartyMember(Character character) {
        this.character = character;
    }

    public int getCharacterID() {
        return getCharacter().getId();
    }

    public String getCharacterName() {
        return getCharacter().getName();
    }

    public short getCharacterJob() {
        return getCharacter().getJob();
    }

    public short getCharacterSubJob() {
        return getCharacter().getSubJob();
    }

    public short getSubSob() {
        return (short) getCharacter().getAvatarData().getCharacterStat().getSubJob();
    }

    public int getCharacterLevel() {
        return getCharacter().getLevel();
    }

    public boolean isOnline() {
        return getCharacter().isOnline();
    }

    public int getChannel() {
        return character.getClient().getChannel();
    }

    public int getFieldID() {
        return character.getFieldID();
    }

    public int getGuildID() {
        return getCharacter().getGuild() != null ? getCharacter().getGuild().getId() : 0;
    }

    public int getGuildRank() {
        return getCharacter().getGuild().getRank();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PartyMember && ((PartyMember) obj).getCharacter().equals(getCharacter());
    }
}
