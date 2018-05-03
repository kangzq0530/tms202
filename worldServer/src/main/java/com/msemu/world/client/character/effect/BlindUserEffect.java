package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class BlindUserEffect implements IUserEffect {

    private boolean on;

    public BlindUserEffect(boolean on) {
        this.on = on;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.BlindEffect;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(on);
    }
}
