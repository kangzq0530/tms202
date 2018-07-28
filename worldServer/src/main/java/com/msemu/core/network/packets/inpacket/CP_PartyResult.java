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

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_PartyResult;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.character.party.PartyMember;
import com.msemu.world.client.character.party.operations.ApplyPartyRejectedResponse;
import com.msemu.world.client.character.party.operations.InvitePartyAlreadyInvitedResponse;
import com.msemu.world.client.character.party.operations.JoinPartyAlreadyFullResponse;
import com.msemu.world.client.field.Field;
import com.msemu.world.enums.PartyOperation;
import com.msemu.world.service.PartyService;

public class CP_PartyResult extends InPacket<GameClient> {

    private int partyId;
    private PartyOperation opcode = PartyOperation.NONE;


    public CP_PartyResult(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

        opcode = PartyOperation.getByValue(decodeByte());
        partyId = decodeInt();
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();
        final PartyService partyService = PartyService.getInstance();
        final Party party = partyService.getPartyByLeaderId(partyId);
        switch (opcode) {
            case PartyRes_InviteParty_Accepted:
                if (partyService.hasInvitation(chr, party)) {
                    if (chr.getParty() != null) {
                        chr.write(new LP_PartyResult(new InvitePartyAlreadyInvitedResponse()));
                    } else if (party.isFull()) {
                        chr.write(new LP_PartyResult(new JoinPartyAlreadyFullResponse()));
                    } else {
                        partyService.removeInvitation(chr);
                        party.addPartyMember(chr);
                    }
                }
                break;
            case PartyRes_ApplyParty_Rejected:
                if (partyService.hasInvitation(chr, party)) {
                    PartyMember leader = party.getPartyLeader();
                    leader.getCharacter().write(new LP_PartyResult(new ApplyPartyRejectedResponse()));
                    partyService.removeInvitation(chr);
                }
                break;
            case PartyRes_ApplyParty_Accepted:
                int x = 1;
                break;
        }

    }
}
