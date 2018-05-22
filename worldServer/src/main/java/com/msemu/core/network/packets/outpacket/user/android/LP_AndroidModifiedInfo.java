package com.msemu.core.network.packets.outpacket.user.android;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Android;

/**
 * Created by Weber on 2018/5/22.
 */
public class LP_AndroidModifiedInfo extends OutPacket<GameClient> {

    public LP_AndroidModifiedInfo(Android android) {
        super((OutHeader.LP_AndroidModified));
        encodeInt(android.getOwner().getId());
        encodeByte(0);
        // unk
        FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME)
                .encode(this);
        android.getAndroidInfo().encode(this);
    }
}
