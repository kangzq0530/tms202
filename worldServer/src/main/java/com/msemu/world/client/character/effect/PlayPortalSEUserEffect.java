package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class PlayPortalSEUserEffect implements IUserEffect {
    @Override
    public UserEffectType getType() {
        return UserEffectType.PlayPortalSE;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

    }
}