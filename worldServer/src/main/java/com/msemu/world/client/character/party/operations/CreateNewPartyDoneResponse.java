package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/5/4.
 */
public class CreateNewPartyDoneResponse implements IPartyResult {

    private Party party;

    public CreateNewPartyDoneResponse(Party party) {
        this.party = party;
    }

    @Override
    public PartyResultType getType() {
        return PartyResultType.PartyRes_CreateNewParty_Done;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(party.getId());
        outPacket.encodeInt(999999999);
        outPacket.encodeInt(999999999);
        outPacket.encodeInt(0);
        outPacket.encodeShort(0);
        outPacket.encodeShort(0);
        outPacket.encodeByte(party.isAppliable());
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        outPacket.encodeString(party.getName());
    }
}
