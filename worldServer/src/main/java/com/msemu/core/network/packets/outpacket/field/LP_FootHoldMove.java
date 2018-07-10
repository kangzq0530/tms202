package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.FieldObj;

import java.util.List;

/**
 * Created by Weber on 2018/5/12.
 */
public class LP_FootHoldMove extends OutPacket<GameClient> {
    public LP_FootHoldMove(List<FieldObj> platforms) {
        super(OutHeader.LP_FootHoldMove);
        encodeInt(platforms.size());
        platforms.forEach(object-> {
            encodeString(object.getName());
            encodeInt(object.getStart());
            encodeInt(object.getSns().size());
            object.getSns().forEach(this::encodeInt);
            encodeInt(object.getSpeed());
            encodeInt(object.getX1());
            encodeInt(object.getX2());
            encodeInt(object.getY1());
            encodeInt(object.getY2());
            encodeInt(object.getPosition().getX());
            encodeInt(object.getPosition().getY());
            encodeByte(object.getR());
            encodeByte(0);
        });
    }
}
