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

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.enums.FieldLimit;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_PartyResult;
import com.msemu.world.Channel;
import com.msemu.world.World;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.character.party.PartyMember;
import com.msemu.world.client.character.party.operations.*;
import com.msemu.world.client.field.Field;
import com.msemu.world.enums.PartyOperation;
import com.msemu.world.service.PartyService;

public class CP_PartyRequest extends InPacket<GameClient> {

    private PartyOperation opcode = PartyOperation.NONE;
    private boolean appliable;
    private boolean setAppliable;
    private String partyName;
    private String targetName;
    private int memberId;
    private int partyID;

    public CP_PartyRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        this.opcode = PartyOperation.getByValue(decodeByte());
        switch (opcode) {
            case PartyReq_CreateNewParty:
                appliable = decodeByte() > 0;
                partyName = decodeString();
                break;
            case PartyReq_WithdrawParty:
                break;
            case PartyReq_JoinParty:
                partyID = decodeInt();
                break;
            case PartyReq_InviteParty:
                targetName = decodeString();
                break;
            case PartyReq_ChangePartyBoss:
                memberId = decodeInt();
                break;
            case PartyReq_ApplyParty:
                partyID = decodeInt();
                break;
            case PartyReq_SetAppliable:
                setAppliable = decodeByte() > 0;
                break;
            case PartyReq_PartySetting:
                appliable = decodeByte() > 0;
                partyName = decodeString();
                break;
            case PartyReq_KickParty:
                memberId = decodeInt();
                break;
            default:
                break;
        }
    }

    @Override
    public void runImpl() {
        PartyService partyService = PartyService.getInstance();
        World world = getClient().getWorld();
        Channel channel = getClient().getChannelInstance();
        Character chr = getClient().getCharacter();
        Field field = chr.getField();
        Party party;
        switch (opcode) {
            case PartyReq_CreateNewParty:
                if (chr.getParty() != null) {
                    chr.write(new LP_PartyResult(new JoinPartyAlreadyJoinedResponse()));
                } else {
                    party = new Party();
                    party.setName(partyName);
                    party.setAppliable(appliable);
                    party.addPartyMember(chr);
                }
                break;
            case PartyReq_WithdrawParty:
                party = chr.getParty();
                if (party != null) {
                    party.withdrawParty(party.getMemberById(chr.getId()));
                }
                break;
            case PartyReq_JoinParty:
                party = partyService.getPartyById(partyID);
                if (party == null) {
                    chr.write(new LP_PartyResult(new JoinPartyUnknownResponse()));
                } else if (party.isFull()) {
                    chr.write(new LP_PartyResult(new JoinPartyAlreadyFullResponse()));
                } else if (chr.getParty() != null) {
                    chr.write(new LP_PartyResult(new JoinPartyAlreadyJoinedResponse()));
                } else {
                    party.addPartyMember(chr);
                }
                break;
            case PartyReq_InviteParty:
                party = chr.getParty();
                Character target = world.getCharacterByName(targetName);
                if (target != null && party != null) {
                    if (partyService.hasInvitation(target, party)) {
                        chr.write(new LP_PartyResult(new InvitePartyAlreadyInvitedResponse()));
                    } else if (party.isFull()) {
                        chr.write(new LP_PartyResult(new JoinPartyAlreadyFullResponse()));
                    } else if (target.getParty() != null) {
                        chr.write(new LP_PartyResult(new JoinPartyAlreadyJoinedResponse()));
                    } else if (target.getClient().getChannel() != getClient().getChannel()) {
                        chr.write(new LP_PartyResult(new PartyMemberInAnotherChanelBlockedUserResponse()));
                    } else {
                        partyService.addInvitation(target, party);
                        chr.write(new LP_PartyResult(new InvitePartySentResponse(targetName)));
                        target.write(new LP_PartyResult(new InvitePartyRequest(chr.getParty(), chr)));
                    }
                }
                break;
            case PartyReq_KickParty:
                party = chr.getParty();
                if (party == null || party.getPartyLeaderId() != chr.getId())
                    return;
                PartyMember expelled = party.getMemberById(memberId);
                if (expelled == null)
                    return;
                if (FieldLimit.ChangePartyBoss.isLimit(field.getFieldLimit())) {
                    chr.write(new LP_PartyResult(new KickPartyFieldLimitResponse()));
                } /*還有個 function limit 不知道是三小*/ else {
                    party.kickPartyMember(expelled);
                    chr.write(new LP_PartyResult(new KickPartyDoneResponse()));
                }
                break;
            case PartyReq_ChangePartyBoss:
                party = chr.getParty();
                if (party == null || party.getPartyLeaderId() != chr.getId())
                    return;
                PartyMember newLeader = party.getMemberById(memberId);
                if (newLeader == null || newLeader.getCharacterID() == party.getPartyLeaderId())
                    return;
                if (FieldLimit.ChangePartyBoss.isLimit(field.getFieldLimit())) {
                    chr.write(new LP_PartyResult(new ChangePartyBossKnownResponse()));
                } else if (newLeader.getChannel() != channel.getChannelId()) {
                    chr.write(new LP_PartyResult(new ChangePartyBossNotSameChannelResponse()));
                } else if (newLeader.getFieldID() != field.getFieldId()) {
                    chr.write(new LP_PartyResult(new ChangePartyBossNotSameFieldResponse()));
                } else {
                    party.changeLeader(newLeader, false);
                }
                break;
            case PartyReq_ApplyParty:
                party = partyService.getPartyById(partyID);
                if (party == null)
                    return;
                PartyMember leader = party.getPartyLeader();
                if (chr.getParty() != null) {
                    chr.write(new LP_PartyResult(new JoinPartyAlreadyJoinedResponse()));
                } else if (leader.getFieldID() != chr.getFieldID()) {
                    // 沒有適合的回應 (不同地圖)
                } else {
                    leader.getCharacter().write(new LP_PartyResult(new ApplyPartyRequest(chr)));
                    chr.write(new LP_PartyResult(new ApplyPartySentResponse()));
                }
                break;
            case PartyReq_PartySetting:
                party = chr.getParty();
                if (chr.getParty().getPartyLeaderId() == chr.getId()) {
                    party.changeSetting(appliable, partyName);
                }
                break;
        }
    }
}
