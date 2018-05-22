package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Weber on 2018/5/22.
 */
public class CP_AndroidEmotion extends InPacket<GameClient> {

    private int emotion;
    private int duration;

    public CP_AndroidEmotion(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        this.emotion = decodeInt();
        this.duration = decodeInt();
    }

    @Override
    public void runImpl() {
        throw new NotImplementedException();
    }
}
