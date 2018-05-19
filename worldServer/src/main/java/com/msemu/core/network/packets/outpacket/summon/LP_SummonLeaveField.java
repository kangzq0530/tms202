package com.msemu.core.network.packets.outpacket.summon;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Summon;
import com.msemu.world.enums.LeaveType;

/**
 * Created by Weber on 2018/4/13.
 */
public class LP_SummonLeaveField extends OutPacket<GameClient> {


    public LP_SummonLeaveField(Summon summon, LeaveType leaveType) {
        super(OutHeader.LP_SummonedLeaveField);
        encodeInt(summon.getCharID());
        encodeInt(summon.getObjectId());
        encodeByte(leaveType.getValue());
    }
}
