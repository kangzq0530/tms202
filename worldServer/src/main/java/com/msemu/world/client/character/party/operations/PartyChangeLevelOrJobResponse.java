package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/5/4.
 */
public class PartyChangeLevelOrJobResponse implements IPartyResult {

    private Character character;

    public PartyChangeLevelOrJobResponse(Character character) {
        this.character = character;
    }

    @Override
    public PartyResultType getType() {
        return PartyResultType.PartyRes_ChangeLevelOrJob;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(character.getId());
        outPacket.encodeInt(character.getLevel());
        outPacket.encodeInt(character.getJob());
        outPacket.encodeInt(character.getSubJob());
    }
}
