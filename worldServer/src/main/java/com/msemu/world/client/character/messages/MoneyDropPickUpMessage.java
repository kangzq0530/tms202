package com.msemu.world.client.character.messages;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.WvsMessageType;

/**
 * Created by Weber on 2018/5/11.
 */
public class MoneyDropPickUpMessage implements IWvsMessage {

    private long money;

    public MoneyDropPickUpMessage(long money) {
        this.money = money;
    }

    @Override
    public WvsMessageType getType() {
        return WvsMessageType.DropPickUpMessage;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(0);
        outPacket.encodeByte(1);
        outPacket.encodeByte(0);
        outPacket.encodeLong(money);
        outPacket.encodeShort(0);
    }
}
