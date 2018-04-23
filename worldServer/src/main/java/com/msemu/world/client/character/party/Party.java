package com.msemu.world.client.character.party;


import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

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

    @Getter
    private final int id;

    @Getter
    private final PartyMember[] partyMembers = new PartyMember[6];

    @Getter
    @Setter
    private boolean appliable;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private int partyLeaderID;
    @Getter
    @Setter
    private Character applyingChar;
    private Map<Integer, Field> fields = new HashMap<>();


    public Party() {
        id = getNewPartyId();

    }

    public boolean isFull() {
        return Arrays.stream(getPartyMembers()).noneMatch(Objects::isNull);
    }

    public boolean isEmpty() {
        return Arrays.stream(getPartyMembers()).allMatch(Objects::isNull);
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
        PartyJoinResult pjr = new PartyJoinResult(this, chr.getName());
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
}
