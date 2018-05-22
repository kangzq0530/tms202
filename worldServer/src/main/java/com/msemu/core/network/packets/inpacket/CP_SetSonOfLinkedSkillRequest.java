package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Weber on 2018/5/22.
 */
public class CP_SetSonOfLinkedSkillRequest extends InPacket<GameClient> {

    public CP_SetSonOfLinkedSkillRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        // 傳授技能
    }

    @Override
    public void runImpl() {
        throw new NotImplementedException();
    }
}
