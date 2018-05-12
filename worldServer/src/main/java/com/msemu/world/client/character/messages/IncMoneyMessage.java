package com.msemu.world.client.character.messages;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.WvsMessageType;

/**
 * Created by Weber on 2018/5/11.
 */
public class IncMoneyMessage implements IWvsMessage {

    private long money;

    public IncMoneyMessage(long money) {
        this.money = money;
    }

    @Override
    public WvsMessageType getType() {
        return WvsMessageType.IncMoneyMessage;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeLong(money);
        outPacket.encodeInt(-1);
    }
}
