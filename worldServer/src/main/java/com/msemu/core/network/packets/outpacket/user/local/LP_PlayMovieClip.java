package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_PlayMovieClip extends OutPacket<GameClient> {
    public LP_PlayMovieClip(String path, boolean show) {
        super(OutHeader.LP_UserPlayMovieClip);
        encodeString(path);
        encodeByte(show);
    }
}
