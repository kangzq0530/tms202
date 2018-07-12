package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.party.operations.IPartyResult;

/**
 * Created by Weber on 2018/5/6.
 */
public class LP_PartyResult extends OutPacket<GameClient> {

    public LP_PartyResult(IPartyResult result) {
        super(OutHeader.LP_PartyResult);
        encodeByte(result.getType().getValue());
        result.encode(this);
    }
}
