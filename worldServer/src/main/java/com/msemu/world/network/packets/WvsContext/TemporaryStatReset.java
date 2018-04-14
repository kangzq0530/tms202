package com.msemu.world.network.packets.WvsContext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.character.skills.TemporaryStatManager;

/**
 * Created by Weber on 2018/4/12.
 */
public class TemporaryStatReset extends OutPacket {

    public TemporaryStatReset(TemporaryStatManager temporaryStatManager, boolean demount) {
        super(OutHeader.TemporaryStatReset);
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
