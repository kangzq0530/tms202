package com.msemu.core.network.packets.in;

import com.msemu.commons.data.templates.field.Portal;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.channel.Channel;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;

/**
 * Created by Weber on 2018/5/2.
 */
public class CP_UserTransferFieldRequest extends InPacket<GameClient> {

    private int targetMapId;

    private String targetPortalName;

    private int tick;

    private short wheelType;

    public CP_UserTransferFieldRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        decodeByte();
        targetMapId = decodeInt();
        targetPortalName = decodeString();
        if (available() >= 7)
            tick = decodeInt();
        decodeByte();
        wheelType = decodeShort();
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        Channel channel = getClient().getChannelInstance();
        Field field = chr.getField();


        if (targetMapId != -1) {

        } else {
            Portal portal = field.getPortalByName(targetPortalName);
            if (portal == null) {
                chr.enableActions();
            } else {
                chr.warpPortal(portal);
            }
        }
    }
}
