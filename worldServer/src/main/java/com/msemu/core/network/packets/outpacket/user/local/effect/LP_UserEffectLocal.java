package com.msemu.core.network.packets.outpacket.user.local.effect;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.effect.IUserEffect;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_UserEffectLocal extends OutPacket<GameClient> {

    public LP_UserEffectLocal() {
        super(OutHeader.LP_UserEffectLocal);
    }

    public LP_UserEffectLocal(IUserEffect userEffect) {
        super(OutHeader.LP_UserEffectLocal);
        encodeByte(userEffect.getType().getValue());
        userEffect.encode(this);
    }
}
