package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.Login.CheckSPWExistResult;

/**
 * Created by Weber on 2018/4/19.
 */
public class CheckSPWExistRequest extends InPacket<LoginClient> {
    public CheckSPWExistRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

    }

    @Override
    public void runImpl() {
        client.write(new CheckSPWExistResult(3, false));
    }
}
