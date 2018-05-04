package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/5/4.
 */
public class PartySetAppliableResponse implements IPartyResult {

    private boolean appliable;

    public PartySetAppliableResponse(boolean appliable) {
        this.appliable = appliable;
    }

    @Override
    public PartyResultType getType() {
        return PartyResultType.PartyRes_SetAppliable;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(appliable);
    }
}
