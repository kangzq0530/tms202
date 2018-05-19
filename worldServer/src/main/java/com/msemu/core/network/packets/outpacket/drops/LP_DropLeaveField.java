package com.msemu.core.network.packets.outpacket.drops;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.DropLeaveType;

/**
 * Created by Weber on 2018/4/13.
 */
public class LP_DropLeaveField extends OutPacket<GameClient> {


    public LP_DropLeaveField(DropLeaveType dropLeaveType, int dropID, int pickupID, short delay, int petID, int slot) {
        super(OutHeader.LP_DropLeaveField);
        encodeByte(dropLeaveType.getValue());
        encodeInt(dropID);
        switch (dropLeaveType) {
            case PickedUpByUser:
            case PickUpByMob:
                encodeInt(pickupID);
                break;
            case PickedUpByPet:
                encodeInt(pickupID);
                encodeInt(petID);
            case Explode:
                encodeShort(delay);
                break;
            case SkillPet:
                encodeInt(slot);
                break;
        }
    }
}
