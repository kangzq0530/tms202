package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.enums.PartyOperation;

/**
 * Created by Weber on 2018/5/4.
 */
public class InvitePartyRequest implements IPartyResult {

    private Party party;
    private Character character;

    public InvitePartyRequest(Party party, Character character) {
        this.party = party;
        this.character = character;
    }

    @Override
    public PartyOperation getType() {
        return PartyOperation.PartyReq_InviteParty;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(character.getId());
        outPacket.encodeString(character.getName());
        outPacket.encodeInt(character.getLevel());
        outPacket.encodeInt(character.getJob());
        outPacket.encodeInt(character.getSubJob());
        outPacket.encodeByte(0);
        outPacket.encodeByte(true);
        outPacket.encodeByte(0);
    }
}
