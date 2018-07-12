package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyOperation;

public class CantNotInThisFieldResponse implements IPartyResult {
    @Override
    public PartyOperation getType() {
        return PartyOperation.PartyRes_CanNotInThisField;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

    }
}
