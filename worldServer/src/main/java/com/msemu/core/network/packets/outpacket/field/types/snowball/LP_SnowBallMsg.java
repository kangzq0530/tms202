package com.msemu.core.network.packets.outpacket.field.types.snowball;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.SnowBallMsg;

/**
 * Created by Weber on 2018/6/1.
 */
public class LP_SnowBallMsg extends OutPacket<GameClient> {

    public LP_SnowBallMsg(int team, SnowBallMsg msg) {
        super(OutHeader.LP_SnowBallMsg);
        encodeByte(team);
        encodeByte(msg.getValue());
    }
}
