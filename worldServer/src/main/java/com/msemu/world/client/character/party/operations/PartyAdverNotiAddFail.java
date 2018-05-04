package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/5/4.
 */
public class PartyAdverNotiAddFail implements IPartyResult {
    @Override
    public PartyResultType getType() {
        return PartyResultType.AdverNoti_AddFail;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

    }
}
