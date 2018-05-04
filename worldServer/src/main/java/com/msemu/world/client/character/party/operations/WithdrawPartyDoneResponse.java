package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/5/4.
 */
public class WithdrawPartyDoneResponse implements IPartyResult {

    private Party party;
    private Character character;

    public WithdrawPartyDoneResponse(Party party, Character character) {
        this.party = party;
        this.character = character;
    }

    @Override
    public PartyResultType getType() {
        // 離開隊伍 || 驅逐隊伍 || 解散隊伍
        return PartyResultType.PartyRes_WithdrawParty_Done;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(party.getId());
        outPacket.encodeInt(character.getId());

    }
}
