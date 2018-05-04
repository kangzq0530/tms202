package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.party.TownPortal;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/5/4.
 */
public class PartyInfoTownPortalChanged implements IPartyResult {

    TownPortal townPortal;

    public PartyInfoTownPortalChanged(TownPortal townPortal) {
        this.townPortal = townPortal;
    }

    @Override
    public PartyResultType getType() {
        return PartyResultType.PartyInfo_TownPortalChanged;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(townPortal.getPortalIndex());
        outPacket.encodeInt(townPortal.getTownID());
        outPacket.encodeInt(townPortal.getFieldID());
        outPacket.encodeInt(townPortal.getSkillID());
        outPacket.encodePosition(townPortal.getPosition());
    }
}
