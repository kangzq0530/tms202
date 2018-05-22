package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Weber on 2018/5/22.
 */
public class CP_UserSelectAndroid extends InPacket<GameClient> {

    private int charId;
    private int templateId;
    private int x, y;

    public CP_UserSelectAndroid(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        this.charId = decodeInt();
        this.templateId = decodeInt();
        this.x = decodeInt();
        this.y = decodeInt();
    }

    @Override
    public void runImpl() {
        throw new NotImplementedException();
    }
}
