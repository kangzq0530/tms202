package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyOperation;

public class InvitePartyAlreadyInvitedResponse implements IPartyResult {
    @Override
    public PartyOperation getType() {
        return PartyOperation.PartyRes_InviteIntrusion_AlreadyInvited;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

    }
}
