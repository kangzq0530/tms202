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
public class CP_UserPortalScriptRequest extends InPacket<GameClient> {

    private String portalName;

    public CP_UserPortalScriptRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        decodeByte();
        portalName = decodeString();
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        Channel channel = getClient().getChannelInstance();
        Portal portal = chr.getField().getPortalByName(portalName);
        if(portal != null) {
            double distance = chr.getPosition().distance(portal.getPosition());
            if(distance < 3000) {
                chr.warpPortal(portal);
            } else {
                chr.chatMessage(String.format("距離地圖太遠了 距離: %f", distance));
            }
        }
    }
}
