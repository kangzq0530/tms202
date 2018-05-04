package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/4/13.
 */
public interface IPartyResult {

    PartyResultType getType();

    void encode(OutPacket<GameClient> outPacket);

}