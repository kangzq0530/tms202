package com.msemu.core.network.packets.outpacket.user.remote;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/5/22.
 */
public class LP_UserSetActiveNickItem extends OutPacket<GameClient> {

    public LP_UserSetActiveNickItem(Character chr, int nickItemId, String unkString) {
        encodeInt(chr.getId());
        encodeInt(nickItemId);
        boolean hasUnkStr = unkString != null && !unkString.isEmpty();
        encodeByte(hasUnkStr);
        if (hasUnkStr) {
            encodeString(unkString);
        }

    }
}
