package com.msemu.core.network.packets.outpacket.user.remote;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.Stat;

public class LP_UserHP extends OutPacket<GameClient> {

    public LP_UserHP(Character chr) {
        super(OutHeader.LP_UserHP);
        encodeInt(chr.getId());
        encodeInt(chr.getStat(Stat.HP));
        encodeInt(chr.getCurrentMaxHp());
    }
}
