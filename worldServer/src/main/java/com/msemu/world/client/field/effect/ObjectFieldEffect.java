package com.msemu.world.client.field.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.FieldEffectType;

/**
 * Created by Weber on 2018/5/2.
 */
public class ObjectFieldEffect implements IFieldEffect {

    private String effect;

    public ObjectFieldEffect(String effect) {
        this.effect = effect;
    }

    @Override
    public FieldEffectType getType() {
        return FieldEffectType.Object;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeString(effect);
    }
}
