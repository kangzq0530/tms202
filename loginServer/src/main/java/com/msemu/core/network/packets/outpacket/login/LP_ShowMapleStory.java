package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;

/**
 * Created by Weber on 2018/4/14.
 */
public class LP_ShowMapleStory extends OutPacket<LoginClient> {

    public LP_ShowMapleStory() {
        super(OutHeader.LP_ShowMapleStory);
        encodeByte(1);
    }
}
