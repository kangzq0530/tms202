package com.msemu.core.network.packets.outpacket.user.common;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/5/16.
 */
public class LP_UserSitResult extends OutPacket<GameClient> {

    public LP_UserSitResult(Character chr, short slotPos) {
        super(OutHeader.LP_UserSitResult);
        encodeInt(chr.getId());
        encodeByte(slotPos != -1);
        if(slotPos > -1)
            encodeShort(slotPos);
    }
}
