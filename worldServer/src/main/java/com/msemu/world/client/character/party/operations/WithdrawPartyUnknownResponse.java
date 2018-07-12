package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyOperation;

public class WithdrawPartyUnknownResponse implements IPartyResult {
    @Override
    public PartyOperation getType() {
        return PartyOperation.PartyRes_WithdrawParty_Unknown;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

    }
}
