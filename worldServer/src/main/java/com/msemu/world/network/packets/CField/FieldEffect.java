package com.msemu.world.network.packets.CField;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.field.effect.IFieldEffect;

/**
 * Created by Weber on 2018/4/13.
 */
public class FieldEffect extends OutPacket {

    public FieldEffect(IFieldEffect fieldEffect) {
        super(OutHeader.FieldEffect);
        encodeByte(fieldEffect.getType().getValue());
        fieldEffect.encode(this);
    }
}
