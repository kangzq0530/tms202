package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/1.
 */
public class LP_IssueReloginCookie extends OutPacket<GameClient> {

    public LP_IssueReloginCookie(String token) {
        super(OutHeader.LP_IssueReloginCookie);
        encodeString(token);
    }
}
