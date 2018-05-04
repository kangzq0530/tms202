package com.msemu.world.client.character.party;


import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.party.operations.JoinPartyDoneResponse;
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
        JoinPartyDoneResponse pjr = new JoinPartyDoneResponse(this, chr.getName());
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
        encode(outPacket, false);
    }

    public void encode(OutPacket<GameClient> outPacket, boolean isLeaving) {
        for (PartyMember pm : partyMembers) {
            outPacket.encodeInt(pm != null ? pm.getCharacterID() : 0);
        }
        for (PartyMember pm : partyMembers) {
            outPacket.encodeString(pm != null ? pm.getCharacterName() : "", 15);
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
            outPacket.encodeInt(pm != null && pm.isOnline() ? pm.getChannel() - 1 : -2);
        }
        outPacket.encodeZeroBytes(6 * 4);
        outPacket.encodeZeroBytes(6 * 4);
        outPacket.encodeInt(getPartyLeaderID());
        for (PartyMember pm : partyMembers) {
            outPacket.encodeInt(pm != null ? pm.getFieldID() : 0);
        }
        for (PartyMember pm : partyMembers) {
            if (pm != null && pm.getTownPortal() != null) {
                pm.getTownPortal().encode(outPacket, isLeaving);
            } else {
                new TownPortal().encode(outPacket, isLeaving);
            }
        }
        outPacket.encodeByte(isAppliable() && !isFull());
        outPacket.encodeByte(false);
        outPacket.encodeString(getName());
        outPacket.encodeByte(false);
        outPacket.encodeByte(false);
        int unkSize = 0;
        for(int i = 0 ; i < unkSize; i++) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeString("");
            outPacket.encodeInt(0);
        }
        for(int i = 0 ; i < 3;i++ ) {
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeLong(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }
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
        for (Field f : getFields().values()) {
            chrs.addAll(f.getChars());
        }
        for (Character chr : chrs) {
            chr.setFieldInstanceType(FieldInstanceType.CHANNEL);
            int returnMap = chr.getField().getReturnMap();
            if (returnMap != 999999999 && returnMap != chr.getField().getReturnMap()) {
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
