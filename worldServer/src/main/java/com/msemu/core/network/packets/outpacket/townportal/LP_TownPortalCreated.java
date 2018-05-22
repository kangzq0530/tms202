package com.msemu.core.network.packets.outpacket.townportal;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.party.TownPortal;

/**
 * Created by Weber on 2018/5/22.
 */
public class LP_TownPortalCreated extends OutPacket<GameClient> {

    public LP_TownPortalCreated(TownPortal townPortal,boolean fadein) {
       super(OutHeader.LP_TownPortalCreated);
        encodeByte(fadein);
        encodeInt(townPortal.getObjectId());
        encodeInt(townPortal.getSkillID());
        encodePosition(townPortal.getPosition());
    }
}
