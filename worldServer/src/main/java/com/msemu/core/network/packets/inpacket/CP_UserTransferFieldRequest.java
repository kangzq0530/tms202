package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.data.templates.field.Portal;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.local.LP_SetDirectionMode;
import com.msemu.core.network.packets.outpacket.user.local.LP_SetInGameDirectionMode;
import com.msemu.world.Channel;
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

            int divi = field.getId() / 100;
            boolean unlock = false;
            boolean warp = false;
            if ((divi / 10 == 1020) && (targetMapId == 1020000 || targetMapId == 4000026)) {
                unlock = true;
                warp = true;
            }
            if (unlock) {
                getClient().write(new LP_SetDirectionMode(false));
                getClient().write(new LP_SetInGameDirectionMode(false));
            }
            if (warp) {
                chr.warp(channel.getField(targetMapId));
            }

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
