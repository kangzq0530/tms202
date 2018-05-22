package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/22.
 */
public class LP_UserEmotionLocal extends OutPacket<GameClient> {

    public LP_UserEmotionLocal(int emotion, int duration, boolean byItemOption) {
        super(OutHeader.LP_UserEmotionLocal);
        encodeInt(emotion);
        encodeInt(duration);
        encodeByte(byItemOption);
    }
}
