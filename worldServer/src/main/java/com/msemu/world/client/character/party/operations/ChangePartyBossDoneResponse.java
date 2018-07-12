package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyOperation;

/**
 * Created by Weber on 2018/5/4.
 */
public class ChangePartyBossDoneResponse implements IPartyResult {

    protected int partyBossCharacterId;
    private boolean dc;

    public ChangePartyBossDoneResponse(int partyBossCharacterId, boolean dc) {
        this.partyBossCharacterId = partyBossCharacterId;
        this.dc = dc;
    }

    @Override
    public PartyOperation getType() {
        return PartyOperation.PartyRes_ChangePartyBoss_Done;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(partyBossCharacterId);
        outPacket.encodeByte(dc);
    }
}
