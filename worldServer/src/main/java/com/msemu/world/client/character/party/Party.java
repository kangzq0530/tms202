package com.msemu.world.client.character.party;


import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.data.FieldData;
import com.msemu.world.enums.FieldInstanceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class Party {

    private static final AtomicInteger partyIdGenerator = new AtomicInteger(1);

    private final int id;

    private final PartyMember[] partyMembers = new PartyMember[6];

    private boolean appliable;
    private String name;
    private int partyLeaderID;
    private Character applyingChar;
    @Getter(value = AccessLevel.PRIVATE)
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

    public void encode(OutPacket<GameClient> outPacket) {
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

    public Field getOrCreateFieldById(int fieldID) {
        if (getFields().containsKey(fieldID)) {
            return getFields().get(fieldID);
        } else {
            Field field = FieldData.getInstance().getFieldFromTemplate(fieldID);
            getFields().put(field.getId(), field);
            return field;
        }
    }

    public void clearFieldInstances() {
        Set<Character> chrs = new HashSet<>();
        for(Field f : getFields().values()) {
            chrs.addAll(f.getChars());
        }
        for(Character chr : chrs) {
            chr.setFieldInstanceType(FieldInstanceType.CHANNEL);
            int returnMap = chr.getField().getReturnMap();
            if(returnMap != 999999999 && returnMap != chr.getField().getReturnMap()) {
                Field field = chr.getClient().getChannelInstance().getField(returnMap);
                chr.warp(field);
            }
        }
        getFields().clear();
    }

    public PartyMember getPartyLeader() {
        return Arrays.stream(getPartyMembers()).filter(p -> p != null && p.getCharacterID() == getPartyLeaderID()).findFirst().orElse(null);
    }
}
