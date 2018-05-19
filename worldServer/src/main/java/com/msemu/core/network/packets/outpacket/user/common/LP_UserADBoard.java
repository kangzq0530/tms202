package com.msemu.core.network.packets.outpacket.user.common;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_UserADBoard extends OutPacket<GameClient> {

    public LP_UserADBoard(Character chr, String message) {
        super(OutHeader.LP_UserADBoard);
        encodeInt(chr.getId());
        if(message == null || message.isEmpty()) {
            encodeByte(false);
        } else {
            encodeByte(true);
            encodeString(message);
        }
    }
}
