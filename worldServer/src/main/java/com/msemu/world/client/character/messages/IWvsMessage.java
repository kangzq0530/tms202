package com.msemu.world.client.character.messages;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.WvsMessageType;

/**
 * Created by Weber on 2018/5/4.
 */
public interface IWvsMessage {
    WvsMessageType getType();
    void encode(OutPacket<GameClient> outPacket);
}
