package com.msemu.login.network.packets.send.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;

/**
 * Created by Weber on 2018/4/14.
 */
public class ShowMapleStory extends OutPacket {

    public ShowMapleStory() {
        super(OutHeader.ShowMapleStory);
        encodeByte(1);
    }
}
