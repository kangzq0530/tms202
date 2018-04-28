package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/28.
 */
public class UserRequestInstanceTable extends InPacket<GameClient> {

    private String tableName;

    private int nCol;

    private int nRow;

    public UserRequestInstanceTable(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        tableName = decodeString();
        nCol = decodeInt();
        nRow = decodeInt();
    }

    @Override
    public void runImpl() {

        // TODO hyperStathandle
        switch (tableName) {
            case "hyper":
            case "hyper_shaman":
                break;
            case "incHyperStat":
            case "needHyperStatLv":
                break;
        }
    }
}
