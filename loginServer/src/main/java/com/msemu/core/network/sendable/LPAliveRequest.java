package com.msemu.core.network.sendable;

import com.msemu.commons.network.SendByteBuffer;
import com.msemu.commons.network.SendablePacket;
import com.msemu.core.network.LoginClient;

/**
 * Created by Weber on 2018/3/16.
 */
public class LPAliveRequest extends SendablePacket<LoginClient> {
    @Override
    protected void writeBody(SendByteBuffer byteBuffer) {
        return;
    }
}
