package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class IncDecHPEffectEXUserEffect implements IUserEffect {

    private int deltaHP;
    private boolean guard;

    public IncDecHPEffectEXUserEffect(int deltaHP, boolean guard) {
        this.deltaHP = deltaHP;
        this.guard = guard;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.IncDecHPEffectEX;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(deltaHP);
        outPacket.encodeByte(guard);
    }
}
