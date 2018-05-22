package com.msemu.core.network.packets.outpacket.user.remote;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/5/22.
 */
public class LP_UserSetActiveEffectItem extends OutPacket<GameClient> {

    public LP_UserSetActiveEffectItem(Character chr, int effectItem) {
        super(OutHeader.LP_UserSetActiveEffectItem);
        encodeInt(chr.getId());
        encodeInt(effectItem);
    }
}
