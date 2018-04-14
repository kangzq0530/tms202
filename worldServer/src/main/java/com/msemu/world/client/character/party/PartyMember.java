package com.msemu.world.client.character.party;

import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/4/13.
 */
public class PartyMember {

    private final Character character;

    private int partyBossCharacterId;

    private TownPortal townPortal;

    public PartyMember(Character character) {
        this.character = character;
    }

    public int getCharacterID() {
        return character.getId();
    }

    public String getCharacterName() {
        return character.getName();
    }

    public short getCharacterJob() {
        return character.getJob();
    }

    public short getCharacterSubJob() {
        return character.getSubJob();
    }

    public short getSubSob() {
        return (short) character.getAvatarData().getCharacterStat().getSubJob();
    }

    public int getCharacterLevel() {
        return character.getLevel();
    }

    public boolean isOnline() {
        return character.isOnline();
    }

    public Character getCharacter() {
        return character;
    }

    public int getPartyBossCharacterID() {
        return partyBossCharacterId;
    }

    public void setPartyBossCharacterID(int partyBossCharacterID) {
        this.partyBossCharacterId = partyBossCharacterID;
    }

    public int getChannel() {
        return character.getClient().getChannelID();
    }

    public int getFieldID() {
        return character.getFieldID();
    }

    public TownPortal getTownPortal() {
        return townPortal;
    }

    public void setTownPortal(TownPortal townPortal) {
        this.townPortal = townPortal;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PartyMember && ((PartyMember) obj).getCharacter().equals(getCharacter());
    }
}
