package com.msemu.core.network.packets.outpacket.user.remote.effect;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.effect.IUserEffect;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_UserEffectRemote extends OutPacket<GameClient> {

    public LP_UserEffectRemote() {
        super(OutHeader.LP_UserEffectRemote);

    }

    public LP_UserEffectRemote(Character chr , IUserEffect effect) {
        encodeInt(chr.getId());
        encodeByte(effect.getType().getValue());
        effect.encode(this);
    }

}
