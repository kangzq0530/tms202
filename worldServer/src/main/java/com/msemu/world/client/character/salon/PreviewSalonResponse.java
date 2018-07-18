package com.msemu.world.client.character.salon;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.BeautySalonOperationType;
import com.msemu.world.enums.SerializedFunctionType;

public class PreviewSalonResponse extends AbstractSalonResponse {

    @Override
    public SerializedFunctionType getFunctionType() {
        return SerializedFunctionType.PREVIEW_SALON_RESPONSE;
    }

    @Override
    public BeautySalonOperationType getType() {
        return BeautySalonOperationType.LOAD;
    }

    @Override
    public void call(OutPacket<GameClient> outPacket) {
        super.call(outPacket);
        outPacket.encodeInt(25631);
        outPacket.encodeByte(0);
        outPacket.encodeInt(3);
        outPacket.encodeInt(3);
        outPacket.encodeInt(3);
        outPacket.encodeZeroBytes(21);
        outPacket.encodeInt(3);
        outPacket.encodeZeroBytes(12);


    }
}
