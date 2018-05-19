package com.msemu.core.network.packets.outpacket.npc;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/3.
 */
public class LP_NpcMove extends OutPacket<GameClient> {

    public LP_NpcMove(int npcObjectID, InPacket<GameClient> inPacket) {
        super(OutHeader.LP_NpcMove);
        encodeInt(npcObjectID);
        encodeByte(inPacket.decodeByte());
        encodeByte(inPacket.decodeByte());
        encodeInt(inPacket.decodeInt());
        if(inPacket.available() > 0) {
            encodeArr(inPacket.decodeBytes(inPacket.available()));
        }
        encodeZeroBytes(100);
    }
}
