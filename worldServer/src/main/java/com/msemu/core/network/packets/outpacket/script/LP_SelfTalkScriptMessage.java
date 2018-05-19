package com.msemu.core.network.packets.outpacket.script;

import com.msemu.world.enums.NpcMessageType;

/**
 * Created by Weber on 2018/5/2.
 */
public class LP_SelfTalkScriptMessage extends LP_ScriptMessage {
    public LP_SelfTalkScriptMessage(String text) {
        super(NpcMessageType.NM_SAY, 3, 0, 0, -1, 3, 0, new String[]{text}, new int[]{0, 1, 0}, null, null);
    }
}
