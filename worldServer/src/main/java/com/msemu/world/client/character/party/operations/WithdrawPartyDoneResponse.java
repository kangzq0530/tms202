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

package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.character.party.PartyMember;
import com.msemu.world.enums.PartyOperation;

/**
 * Created by Weber on 2018/5/4.
 */
public class WithdrawPartyDoneResponse implements IPartyResult {

    private Party party;
    private PartyMember partyMember;
    private boolean disband;
    private boolean forceLeaving;

    public WithdrawPartyDoneResponse(Party party, PartyMember partyMember, boolean disband, boolean forceLeaving) {
        this.party = party;
        this.partyMember = partyMember;
        this.disband = disband;
        this.forceLeaving = forceLeaving;
    }

    @Override
    public PartyOperation getType() {
        // 離開隊伍 || 驅逐隊伍 || 解散隊伍
        return PartyOperation.PartyRes_WithdrawParty_Done;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(party.getId());
        outPacket.encodeInt(partyMember.getCharacterID());
        outPacket.encodeByte(!disband);
        outPacket.encodeByte(forceLeaving);
        outPacket.encodeString(partyMember.getCharacterName());
        party.encode(outPacket, true);

    }
}
