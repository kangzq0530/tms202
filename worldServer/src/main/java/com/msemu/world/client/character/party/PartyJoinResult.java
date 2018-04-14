package com.msemu.world.client.character.party;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/4/13.
 */
public class PartyJoinResult implements PartyResultInfo {

    public Party party;
    public String joinerName;

    @Override
    public PartyResultType getType() {
        return PartyResultType.Join;
    }

    @Override
    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(party.getId());
        outPacket.encodeString(joinerName);
        party.encode(outPacket);
    }
}

