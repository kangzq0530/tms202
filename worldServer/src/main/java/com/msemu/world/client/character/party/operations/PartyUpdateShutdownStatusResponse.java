package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/5/4.
 */
public class PartyUpdateShutdownStatusResponse implements IPartyResult {

    private Character character;

    private boolean shutdown;

    public PartyUpdateShutdownStatusResponse(Character character, boolean shutdown) {
        this.character = character;
        this.shutdown = shutdown;
    }

    @Override
    public PartyResultType getType() {
        return PartyResultType.PartyRes_UpdateShutdownStatus;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(character.getId());
        outPacket.encodeByte(shutdown);
    }
}
