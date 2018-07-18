package com.msemu.world.client.character.salon;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.utils.ISerializedFunction;
import com.msemu.world.enums.BeautySalonOperationType;

public abstract class AbstractSalonResponse extends ISerializedFunction<OutPacket<GameClient>> {

    public abstract BeautySalonOperationType getType();


    @Override
    public void call(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(getType().getValue());
        outPacket.encodeInt(1);
    }
}
