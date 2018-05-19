package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_BallonMsg extends OutPacket<GameClient> {

    public LP_BallonMsg(String message) {
        this(message, 200, 5, null);
    }

    public LP_BallonMsg(String message, int width, int height, Position position) {
        super(OutHeader.LP_UserBalloonMsg);
        encodeString(message);
        encodeShort(width);
        encodeShort(height);
        encodeByte(position == null);
        if(position != null) {
            encodeInt(position.getX());
            encodeInt(position.getY());
        }
    }
}
