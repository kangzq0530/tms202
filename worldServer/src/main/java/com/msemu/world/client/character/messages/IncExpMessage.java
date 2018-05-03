package com.msemu.world.client.character.messages;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.ExpIncreaseInfo;
import com.msemu.world.enums.WvsMessageType;

/**
 * Created by Weber on 2018/5/4.
 */
public class IncExpMessage implements IWvsMessage {

    private ExpIncreaseInfo expIncreaseInfo;

    public IncExpMessage(ExpIncreaseInfo expIncreaseInfo) {
        this.expIncreaseInfo = expIncreaseInfo;
    }

    @Override
    public WvsMessageType getType() {
        return WvsMessageType.IncEXPMessage;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        expIncreaseInfo.encode(outPacket);
    }
}
