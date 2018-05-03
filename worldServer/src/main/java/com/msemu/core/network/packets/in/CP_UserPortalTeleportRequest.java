package com.msemu.core.network.packets.in;

import com.msemu.commons.data.templates.field.Portal;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/5/2.
 */
public class CP_UserPortalTeleportRequest extends InPacket<GameClient> {

    private byte unk;
    private String portalName;
    private short toX;
    private short toY;

    public CP_UserPortalTeleportRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        unk = decodeByte();
        portalName = decodeString();
        toX = decodeShort();
        toY  = decodeShort();
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        Portal portal = chr.getField().getPortalByName(portalName);

        if(portal != null) {
            chr.setPosition(new Position(toX, toY));
        }

        // TODO check faraway portal attack

    }
}
