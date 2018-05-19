package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.skill.TemporaryStatManager;

/**
 * Created by Weber on 2018/4/12.
 */
public class LP_TemporaryStatReset extends OutPacket<GameClient> {

    public LP_TemporaryStatReset(TemporaryStatManager temporaryStatManager, boolean demount) {
        super(OutHeader.LP_TemporaryStatReset);
        for (int i : temporaryStatManager.getRemovedMask()) {
            encodeInt(i);
        }
        temporaryStatManager.encodeRemovedIndieTempStat(this);
        if (temporaryStatManager.hasRemovedMovingEffectingStat()) {
            encodeByte(0);
        }
        encodeByte(0); // ?
        encodeByte(demount);
        temporaryStatManager.getRemovedStats().clear();
    }
}
