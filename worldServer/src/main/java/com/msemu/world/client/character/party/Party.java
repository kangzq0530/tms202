package com.msemu.world.client.character.party;


import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.character.Character;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Weber on 2018/4/13.
 */
public class Party {

    private static final AtomicInteger partyIdGenerator = new AtomicInteger(1);

    private final int id;

    private final PartyMember[] partyMembers = new PartyMember[6];

    private boolean appliable;
    private String name;
    private int partyLeaderID;
    private Character applyingChar;
    private Map<Integer, Field> fields = new HashMap<>();


    public Party() {
        id = getNewPartyId();

    }


    public boolean isAppliable() {
        return appliable;
    }

    public void setAppliable(boolean appliable) {
        this.appliable = appliable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PartyMember[] getPartyMembers() {
        return partyMembers;
    }

    public boolean isFull() {
        return Arrays.stream(getPartyMembers()).noneMatch(Objects::isNull);
    }

    public boolean isEmpty() {
        return Arrays.stream(getPartyMembers()).allMatch(Objects::isNull);
    }

    public void encode(OutPacket outPacket) {
        for (PartyMember pm : partyMembers) {
            outPacket.encodeInt(pm != null ? pm.getCharacterID() : 0);
        }
        for (PartyMember pm : partyMembers) {
            outPacket.encodeString(pm != null ? pm.getCharacterName() : "", 13);
        }
        for (PartyMember pm : partyMembers) {
            outPacket.encodeInt(pm != null ? pm.getCharacterJob() : 0);
        }
        for (PartyMember pm : partyMembers) {
            outPacket.encodeInt(pm != null ? pm.getCharacterSubJob() : 0);
        }
        for (PartyMember pm : partyMembers) {
            outPacket.encodeInt(pm != null ? pm.getCharacterLevel() : 0);
        }
        for (PartyMember pm : partyMembers) {
            outPacket.encodeInt(pm != null ? pm.getChannel() - 1 : 0);
        }
        for (PartyMember pm : partyMembers) {
            outPacket.encodeInt(pm != null && pm.isOnline() ? 1 : 0);
        }
//        for(PartyMember pm : partyMembers) {
        outPacket.encodeInt(getPartyLeaderID());
//        }
        // end PARTYMEMBER struct
        for (PartyMember pm : partyMembers) {
            outPacket.encodeInt(pm != null ? pm.getFieldID() : 0);
        }
        for (PartyMember pm : partyMembers) {
            if (pm != null && pm.getTownPortal() != null) {
                pm.getTownPortal().encode(outPacket);
            } else {
                new TownPortal().encode(outPacket);
            }
        }
        outPacket.encodeByte(isAppliable() && !isFull());
        outPacket.encodeString(getName());
        outPacket.encodeArr(new byte[50]);
    }

    public int getPartyLeaderID() {
        return partyLeaderID;
    }

    public void setPartyLeaderID(int partyLeaderID) {
        this.partyLeaderID = partyLeaderID;
    }

    public int getId() {
        return id;
    }

    public void addPartyMember(Character chr) {
        if (isFull()) {
            return;
        }
        PartyMember pm = new PartyMember(chr);
        if (isEmpty()) {
            setPartyLeaderID(chr.getId());
        }
        PartyMember[] partyMembers = getPartyMembers();
        PartyJoinResult pjr = new PartyJoinResult();
        pjr.party = this;
        pjr.joinerName = chr.getName();
        for (int i = 0; i < partyMembers.length; i++) {
            if (partyMembers[i] == null) {
                partyMembers[i] = pm;
                chr.setParty(this);
                break;
            }
        }
    }


    private int getNewPartyId() {
        return partyIdGenerator.getAndIncrement();
    }

    public void updateFull() {
    }
}
