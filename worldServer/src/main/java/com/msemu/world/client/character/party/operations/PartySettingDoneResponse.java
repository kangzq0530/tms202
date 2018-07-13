package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyOperation;

/**
 * Created by Weber on 2018/5/4.
 */
public class PartySettingDoneResponse implements IPartyResult {

    private boolean privateParty;
    private String partyName;

    public PartySettingDoneResponse(boolean appliable, String partyName) {
        this.privateParty = appliable;
        this.partyName = partyName;
    }

    @Override
    public PartyOperation getType() {
        return PartyOperation.PartyRes_PartySettingDone;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(privateParty);
        outPacket.encodeString(partyName);

    }
}
