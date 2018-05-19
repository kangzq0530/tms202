package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_TutorMsg extends OutPacket<GameClient> {

    public LP_TutorMsg() {
        super(OutHeader.LP_UserTutorMsg);
        throw new NotImplementedException();
    }

}
