package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.Account;

/**
 * Created by Weber on 2018/5/1.
 */
public class RequestReloginCookie extends InPacket<GameClient> {

    private String username;

    public RequestReloginCookie(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        username = decodeString();
    }

    @Override
    public void runImpl() {

        Account account = getClient().getAccount();

        if(!account.getUsername().equals(username)) {
            getClient().close();
            return;
        }






    }
}
