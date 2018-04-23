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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PartyMember && ((PartyMember) obj).getCharacter().equals(getCharacter());
    }
}
