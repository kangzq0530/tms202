package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.Login.ShowMapleStory;

/**
 * Created by Weber on 2018/4/19.
 */
public class ClientStart extends InPacket<LoginClient> {
    public ClientStart(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

    }

    @Override
    public void runImpl() {
        client.write(new ShowMapleStory());
    }
}
