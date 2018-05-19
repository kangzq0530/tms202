package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.FaceEmotion;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_EmotionLocal extends OutPacket<GameClient> {

    public LP_EmotionLocal(FaceEmotion emotion, int duration) {
        super(OutHeader.LP_UserEmotionLocal);

        encodeInt(emotion.getValue());
        encodeInt(duration);
        encodeByte(false);
    }
}
