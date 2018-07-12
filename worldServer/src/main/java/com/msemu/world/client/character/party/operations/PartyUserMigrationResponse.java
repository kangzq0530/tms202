package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.enums.PartyOperation;

/**
 * Created by Weber on 2018/5/4.
 */
public class PartyUserMigrationResponse implements IPartyResult {

    private Party party;

    public PartyUserMigrationResponse(Party party) {
        this.party = party;
    }

    @Override
    public PartyOperation getType() {
        return PartyOperation.PartyRes_UserMigration;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(party.getId());
        party.encode(outPacket);
    }
}
