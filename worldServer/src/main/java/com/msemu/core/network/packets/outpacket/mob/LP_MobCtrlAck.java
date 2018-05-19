package com.msemu.core.network.packets.outpacket.mob;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Mob;

/**
 * Created by Weber on 2018/5/4.
 */
public class LP_MobCtrlAck extends OutPacket<GameClient> {

    public LP_MobCtrlAck(Mob mob, boolean nextAttackPossible, short moveID, int skillID, byte slv, int forcedAttack) {
        super(OutHeader.LP_MobCtrlAck);
        encodeInt(mob.getObjectId());
        encodeShort(moveID);
        encodeByte(nextAttackPossible);
        encodeInt((int) mob.getMp());
        encodeInt(skillID);
        encodeByte(slv);
        encodeInt(forcedAttack);
    }
}
