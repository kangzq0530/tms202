package com.msemu.world.network.packets.SummonPool;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.life.Summon;
import com.msemu.world.enums.LeaveType;

/**
 * Created by Weber on 2018/4/13.
 */
public class SummonLeaveField extends OutPacket {


    public SummonLeaveField(Summon summon, LeaveType leaveType) {
        super(OutHeader.SummonedLeaveField);
        encodeInt(summon.getCharID());
        encodeInt(summon.getObjectId());
        encodeByte(leaveType.getValue());
    }
}
