package com.msemu.core.network.packets.outpacket.field.types.snowball;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.types.snowball.SnowBallField;

/**
 * Created by Weber on 2018/6/1.
 */
public class LP_SnowBallState extends OutPacket<GameClient> {

    public LP_SnowBallState(SnowBallField field, boolean first) {
        super(OutHeader.LP_SnowBallState);
        encodeByte(field.getState());
        encodeInt(field.getSnowMans()[0] != null ? field.getSnowMans()[0].getHp() : 0);
        encodeInt(field.getSnowMans()[1] != null ? field.getSnowMans()[1].getHp() : 0);
        for(int i = 0 ; i < 2; i++) {
            encodeShort(field.getSnowBalls()[i].getXPos());
            encodeByte(0); // unknown
        }
        if(first) {
//            SnowBallInfo snowBallInfo = field.getFieldData().getSnowBallInfo();
//            encodeShort(snowBallInfo.getDamageSnowBall());
//            encodeShort(snowBallInfo.getDamageSnowMan0());
//            encodeShort(snowBallInfo.getDamageSnowMan1());
        }
    }
}
