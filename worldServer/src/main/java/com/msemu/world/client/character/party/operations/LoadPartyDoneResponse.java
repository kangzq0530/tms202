package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/5/4.
 */
public class LoadPartyDoneResponse implements IPartyResult {

    private Party party;

    public LoadPartyDoneResponse(Party party) {
        this.party = party;
    }

    @Override
    public PartyResultType getType() {
        return PartyResultType.PartyRes_LoadParty_Done;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(party.getId());
        party.encode(outPacket);
    }
}
