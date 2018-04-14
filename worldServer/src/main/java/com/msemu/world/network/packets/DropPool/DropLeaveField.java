package com.msemu.world.network.packets.DropPool;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.enums.DropLeaveType;

/**
 * Created by Weber on 2018/4/13.
 */
public class DropLeaveField extends OutPacket {

    public DropLeaveField(int dropID, int pickUpUserID) {
        this(DropLeaveType.CHAR_PICKUP_1, pickUpUserID, dropID, (short) 0, 0, 0);
    }

    public DropLeaveField(DropLeaveType dropLeaveType, int pickupID, int dropID, short delay, int petID, int key) {
        super(OutHeader.DropLeaveField);
        encodeByte(dropLeaveType.getValue());
        encodeInt(dropID);
        switch (dropLeaveType) {
            case CHAR_PICKUP_1:
            case CHAR_PICKUP_2:
                encodeInt(pickupID);
                break;
            case PET_PICKUP:
                encodeInt(pickupID);
                encodeInt(petID);
            case DELAYED_PICKUP:
                encodeShort(delay);
                break;
            case IDK_3:
                encodeInt(key);
                break;
        }
    }
}
