package com.msemu.world.client.character.party;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyResultType;
import lombok.Getter;

/**
 * Created by Weber on 2018/4/13.
 */
public class PartyJoinResult implements PartyResultInfo {

    @Getter
    public Party party;
    @Getter
    public String joinerName;

    public PartyJoinResult(Party party, String joinerName) {
        this.party = party;
        this.joinerName = joinerName;
    }

    @Override
    public PartyResultType getType() {
        return PartyResultType.Join;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(party.getId());
        outPacket.encodeString(joinerName);
        party.encode(outPacket);
    }
}

