package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/4/23.
 */
public class LP_SetTamingMobInfo extends OutPacket<GameClient> {
    public LP_SetTamingMobInfo(Character character, boolean isLevelup) {
        super(OutHeader.LP_SetTamingMobInfo);
        encodeInt(character.getId());
        encodeInt(character.getTamingMobLevel());
        encodeInt(character.getTamingMobExp());
        encodeInt(character.getTamingMobFatigue());
        encodeByte(isLevelup);
    }
}
