package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/5/4.
 */
public class InviteIntrusionRequest implements IPartyResult {

    private Character character;

    public InviteIntrusionRequest(Character character) {
        this.character = character;
    }

    @Override
    public PartyResultType getType() {
        return PartyResultType.PartyReq_InviteIntrusion;
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
