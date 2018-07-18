package com.msemu.world.client.character.messages;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PickUpMessageType;
import com.msemu.world.enums.WvsMessageType;

/**
 * Created by Weber on 2018/5/11.
 */
public class MoneyDropPickUpMessage extends DropPickUpMessage {

    private long money;

    public MoneyDropPickUpMessage(long money) {
        this.money = money;
    }

    @Override
    public PickUpMessageType getDropPickUpType() {
        return PickUpMessageType.MESSO;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        super.encode(outPacket);
        outPacket.encodeByte(0);
        outPacket.encodeLong(money);
        outPacket.encodeShort(0);
    }
}
