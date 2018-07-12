package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.PartyOperation;

/**
 * Created by Weber on 2018/5/4.
 */
public class ApplyPartyRequest implements IPartyResult {

    private Character character;

    public ApplyPartyRequest(Character character) {
        this.character = character;
    }

    @Override
    public PartyOperation getType() {
        return PartyOperation.PartyReq_ApplyParty;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(character.getId());
        outPacket.encodeString(character.getName());
        outPacket.encodeInt(character.getLevel());
        outPacket.encodeInt(character.getJob());
        outPacket.encodeInt(character.getSubJob());
    }
}
