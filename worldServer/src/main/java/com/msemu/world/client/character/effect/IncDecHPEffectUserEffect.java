package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class IncDecHPEffectUserEffect implements IUserEffect {

    private int deltaHP;

    public IncDecHPEffectUserEffect(int deltaHP) {
        this.deltaHP = deltaHP;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.IncDecHPEffect;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(deltaHP);
    }
}
