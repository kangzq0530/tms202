package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyOperation;

public class ApplyPartyAlreadyAppliedByApplierResponse implements IPartyResult {
    @Override
    public PartyOperation getType() {
        return PartyOperation.PartyRes_ApplyParty_AlreadyAppliedByApplier;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

    }
}
