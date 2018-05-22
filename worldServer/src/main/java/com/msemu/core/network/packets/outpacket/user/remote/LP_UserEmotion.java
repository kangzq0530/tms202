package com.msemu.core.network.packets.outpacket.user.remote;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/5/22.
 */
public class LP_UserEmotion extends OutPacket<GameClient> {

    public LP_UserEmotion(Character fromChr, int motion, int duration, boolean byItemOption) {
        super(OutHeader.LP_UserEmotion);

        encodeInt(fromChr.getId());
        encodeInt(motion);
        encodeInt(duration);
        encodeByte(byItemOption);
    }
}
