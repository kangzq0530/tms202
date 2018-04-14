package com.msemu.world.client.field.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.enums.FieldEffectType;

/**
 * Created by Weber on 2018/4/13.
 */
public interface IFieldEffect {

    FieldEffectType getType();

    void encode(OutPacket outPacket);
}
