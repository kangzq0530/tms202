package com.msemu.world.client.field.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.FieldEffectType;

/**
 * Created by Weber on 2018/5/2.
 */
public class ScreenDelayFieldEffect implements IFieldEffect {

    private String effect;

    private int delay;

    public ScreenDelayFieldEffect(String effect, int delay) {
        this.effect = effect;
        this.delay = delay;
    }

    @Override
    public FieldEffectType getType() {
        return FieldEffectType.ScreenDelayed;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeString(effect);
        outPacket.encodeInt(delay);
    }
}
