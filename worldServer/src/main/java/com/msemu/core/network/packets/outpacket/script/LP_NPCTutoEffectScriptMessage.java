package com.msemu.core.network.packets.outpacket.script;

import com.msemu.world.enums.NpcMessageType;

/**
 * Created by Weber on 2018/5/2.
 */
public class LP_NPCTutoEffectScriptMessage extends LP_ScriptMessage {
    public LP_NPCTutoEffectScriptMessage(String... effect) {
        super(NpcMessageType.NM_SAY_IMAGE, 3, 0, -1, -1, 1, 0, effect, new int[]{1}, null, null);
    }
}
