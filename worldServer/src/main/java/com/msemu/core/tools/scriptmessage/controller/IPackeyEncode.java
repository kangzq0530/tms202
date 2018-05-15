package com.msemu.core.tools.scriptmessage.controller;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/15.
 */
public interface IPackeyEncode {
    void encode(OutPacket<GameClient> outPacket);

    void setOverrideTemplateIDChangeListener(IOverrideTemplateIDChange listener);
}
