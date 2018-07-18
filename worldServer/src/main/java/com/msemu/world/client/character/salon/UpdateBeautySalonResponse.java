package com.msemu.world.client.character.salon;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.BeautySalonOperationType;
import com.msemu.world.enums.SerializedFunctionType;

public class UpdateBeautySalonResponse extends AbstractSalonResponse {
    @Override
    public BeautySalonOperationType getType() {
        return BeautySalonOperationType.CHANGE;
    }

    @Override
    public SerializedFunctionType getFunctionType() {
        return SerializedFunctionType.CHANGE_SALON_RESPONSE;
    }

    @Override
    public void call(OutPacket<GameClient> outPacket) {
        super.call(outPacket);
    }
}
