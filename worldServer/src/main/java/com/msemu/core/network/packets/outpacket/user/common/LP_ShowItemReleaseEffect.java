package com.msemu.core.network.packets.outpacket.user.common;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;

public class LP_ShowItemReleaseEffect extends OutPacket<GameClient> {

    public LP_ShowItemReleaseEffect(final Character chr, final short pos, final boolean bonus) {
        super(OutHeader.LP_ShowItemReleaseEffect);
        encodeInt(chr.getId());
        encodeShort(pos);
        encodeByte(bonus);
    }

}
