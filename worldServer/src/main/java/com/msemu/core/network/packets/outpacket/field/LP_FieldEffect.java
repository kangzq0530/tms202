package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.effect.IFieldEffect;

/**
 * Created by Weber on 2018/4/13.
 */
public class LP_FieldEffect extends OutPacket<GameClient> {

    public LP_FieldEffect(IFieldEffect fieldEffect) {
        super(OutHeader.LP_FieldEffect);
        encodeByte(fieldEffect.getType().getValue());
        fieldEffect.encode(this);
    }
}
