package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.InstanceTableInfo;

/**
 * Created by Weber on 2018/4/28.
 */
public class LP_ResultInstanceTable extends OutPacket<GameClient> {

    public LP_ResultInstanceTable(String tableName , int nCol, int nRow, InstanceTableInfo tableInfo) {
        super(OutHeader.LP_ResultInstanceTable);
        encodeString(tableName);
        encodeInt(nCol);
        encodeInt(nRow);
        encodeByte(tableInfo.isRightResult());
        encodeInt(tableInfo.getValue());
    }
}
