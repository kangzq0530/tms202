package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.party.TownPortal;
import com.msemu.world.client.field.Field;
import com.msemu.world.enums.FieldObjectType;

/**
 * Created by Weber on 2018/5/2.
 */
public class CP_EnterTownPortalRequest extends InPacket<GameClient> {

    private int portalObjectID;
    private boolean fromTown;


    public CP_EnterTownPortalRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        portalObjectID = decodeInt();
        fromTown = decodeByte() > 0;
    }

    @Override
    public void runImpl() {

        Character chr = getClient().getCharacter();
        Field field = chr.getField();

        TownPortal portal = (TownPortal) field.getFieldObjectsByKey(FieldObjectType.TOWN_PORTAL, portalObjectID);

        if (portal != null) {
            portal.warp(chr, fromTown);
        }

    }
}
