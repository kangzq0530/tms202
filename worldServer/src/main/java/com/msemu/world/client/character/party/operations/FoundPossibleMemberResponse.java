package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyOperation;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Weber on 2018/5/4.
 */
public class FoundPossibleMemberResponse implements IPartyResult {


    @Override
    public PartyOperation getType() {
        return PartyOperation.PartyRes_FoundPossibleMember;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        // s Name
        // 4 4 4
        // 4 - partyID
        throw new NotImplementedException();
    }
}
