package com.msemu.world.client.character.party;


import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_PartyResult;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.party.operations.CreateNewPartyDoneResponse;
import com.msemu.world.client.character.party.operations.JoinPartyDoneResponse;
import com.msemu.world.client.character.party.operations.LoadPartyDoneResponse;
import com.msemu.world.client.character.party.operations.WithdrawPartyDoneResponse;
import com.msemu.world.client.field.Field;
import com.msemu.world.data.FieldData;
import com.msemu.world.enums.FieldInstanceType;
import com.msemu.world.service.PartyService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
        boolean empty = isEmpty();
        PartyMember pm = new PartyMember(chr);
        PartyMember[] partyMembers = getPartyMembers();
        for (int i = 0; i < partyMembers.length; i++) {
            if (partyMembers[i] == null) {
                partyMembers[i] = pm;
                chr.setParty(this);
                break;
            }
        }
        if (empty) {
            changeLeader(pm);
            chr.write(new LP_PartyResult(new CreateNewPartyDoneResponse(this)));
        }
        JoinPartyDoneResponse pjr = new JoinPartyDoneResponse(this, chr.getName());
        getOnlineMembers().forEach(partyMember -> partyMember.getCharacter().write(new LP_PartyResult(pjr)));
        updateFull();
    }

    public void withdrawParty() {
        List<PartyMember> members = getOnlineMembers();
        PartyMember leader = getPartyLeader();
        for (int i = 0; i < partyMembers.length; i++) {
            if (partyMembers[i] != null) {
                partyMembers[i].getCharacter().setParty(null);
                partyMembers[i] = null;
            }
        }
        WithdrawPartyDoneResponse wpr = new WithdrawPartyDoneResponse(this, leader, true);
        members.forEach(partyMember -> partyMember.getCharacter().write(new LP_PartyResult(wpr)));
        PartyService.getInstance().removeParty(this);
    }

    private int getNewPartyId() {
        return partyIdGenerator.getAndIncrement();
    }


    public List<PartyMember> getOnlineMembers() {
        return Arrays.stream(getPartyMembers()).filter(pm -> pm != null && pm.isOnline()).collect(Collectors.toList());
    }

    public List<PartyMember> getAllMembers() {
        return Arrays.stream(getPartyMembers()).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void updateFull() {
        LoadPartyDoneResponse upr = new LoadPartyDoneResponse(this);
        OutPacket<GameClient> outPacket = new LP_PartyResult(upr);
        for (PartyMember pm : getOnlineMembers()) {
            pm.getCharacter().write(outPacket);
        }
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
        outPacket.encodeInt(getPartyLeaderID());
        for (PartyMember pm : partyMembers) {
            outPacket.encodeInt(pm != null ? pm.getFieldID() : 0);
        }
        final TownPortal emptyTownPortal = new TownPortal();
        for (PartyMember pm : partyMembers) {
            if (pm != null && pm.getTownPortal() != null) {
                pm.getTownPortal().encodeForTown(outPacket, isLeaving);
            } else {
                emptyTownPortal.encodeForTown(outPacket, isLeaving);
            }
        }
        outPacket.encodeByte(isAppliable() && !isFull());
        outPacket.encodeByte(false);
        outPacket.encodeString(getName());
        //
        outPacket.encodeByte(false);
        outPacket.encodeByte(false);
        int unkSize = 0;
        outPacket.encodeByte(unkSize);
        for (int i = 0; i < unkSize; i++) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeString("");
            outPacket.encodeInt(0);
        }
        for (int i = 0; i < 3; i++) {
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
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
            getFields().put(field.getFieldId(), field);
            return field;
        }
    }

    public void clearFieldInstances() {
        Set<Character> chrs = new HashSet<>();
        for (Field field : getFields().values()) {
            chrs.addAll(field.getAllCharacters());
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

    public PartyMember getMemberById(int memberId) {
        return getAllMembers().stream().filter(partyMember -> partyMember.getCharacterID() == memberId)
                .findFirst().orElse(null);
    }

    public void removePartyMember(PartyMember memberToKick) {
        for (int i = 0; i < partyMembers.length; i++) {
            if (partyMembers[i] != null && partyMembers[i] == memberToKick) {
                partyMembers[i].getCharacter().setParty(null);
                partyMembers[i] = null;
                memberToKick.getCharacter().write(new LP_PartyResult(new WithdrawPartyDoneResponse(this, memberToKick, false)));
                break;
            }
        }
        if(isEmpty() || getPartyLeaderID() == memberToKick.getCharacterID())
            withdrawParty();
        updateFull();
    }

    public void changeLeader(PartyMember newLeader) {
        setPartyLeaderID(newLeader.getCharacterID());
        for (PartyMember partyMember : partyMembers) {
            if (partyMember != null) {
                partyMember.setPartyLeaderId(newLeader.getCharacterID());
            }
        }
        updateFull();
    }
}
