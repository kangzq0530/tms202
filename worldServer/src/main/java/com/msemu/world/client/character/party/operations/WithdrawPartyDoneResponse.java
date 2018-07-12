package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.character.party.PartyMember;
import com.msemu.world.enums.PartyOperation;

/**
 * Created by Weber on 2018/5/4.
 */
public class WithdrawPartyDoneResponse implements IPartyResult {

    private Party party;
    private PartyMember partyMember;
    private boolean dispand;

    public WithdrawPartyDoneResponse(Party party, PartyMember partyMember, boolean disband) {
        this.party = party;
        this.partyMember = partyMember;
        this.dispand = disband;
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
        outPacket.encodeByte(!dispand);
        outPacket.encodeByte(1);
        outPacket.encodeString(partyMember.getCharacterName());
        party.encode(outPacket, true );

    }
}
