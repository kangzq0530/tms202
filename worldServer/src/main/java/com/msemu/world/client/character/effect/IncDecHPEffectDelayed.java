package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class IncDecHPEffectDelayed implements IUserEffect {

    private int deltaHP;
    private int delay;

    public IncDecHPEffectDelayed(int deltaHP, int delay) {
        this.deltaHP = deltaHP;
        this.delay = delay;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.IncDecHPEffectDelayed;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(deltaHP);
        outPacket.encodeInt(delay);
    }
}
