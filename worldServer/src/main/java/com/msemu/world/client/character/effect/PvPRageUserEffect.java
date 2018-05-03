package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class PvPRageUserEffect implements IUserEffect {

    private int duration;

    public PvPRageUserEffect(int duration) {
        this.duration = duration;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.PvPRage;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(duration);
    }
}
