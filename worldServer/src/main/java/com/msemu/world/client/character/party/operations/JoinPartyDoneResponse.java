package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.enums.PartyOperation;
import lombok.Getter;

/**
 * Created by Weber on 2018/4/13.
 */
public class JoinPartyDoneResponse implements IPartyResult {

    @Getter
    public Party party;
    @Getter
    public String joinerName;

    public JoinPartyDoneResponse(Party party, String joinerName) {
        this.party = party;
        this.joinerName = joinerName;
    }

    @Override
    public PartyOperation getType() {
        return PartyOperation.PartyRes_JoinParty_Done;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(party.getId());
        outPacket.encodeString(joinerName);
        outPacket.encodeByte(false);
        outPacket.encodeInt(0);
        getParty().encode(outPacket);
    }
}

