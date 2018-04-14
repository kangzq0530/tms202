package com.msemu.world.client.character.party;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/4/13.
 */
public interface PartyResultInfo {

    PartyResultType getType();

    void encode(OutPacket outPacket);

}