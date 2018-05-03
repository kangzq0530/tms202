package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.out.wvscontext.LP_IssueReloginCookie;
import com.msemu.world.client.Account;
import com.msemu.world.service.LoginCookieService;

import java.rmi.RemoteException;

/**
 * Created by Weber on 2018/5/1.
 */
public class CP_RequestReloginCookie extends InPacket<GameClient> {

    private String username;

    public CP_RequestReloginCookie(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        username = decodeString();
    }

    @Override
    public void runImpl() {

        Account account = getClient().getAccount();

        if(!account.getSecureUserName().equals(username)) {
            getClient().close();
            return;
        }

        String token = null;
        try {
            token = LoginCookieService.getInstance().generateToken(account.getUsername(), getClient().getWorld().getWorldId(), getClient().getChannel());
            getClient().write(new LP_IssueReloginCookie(token));
        } catch (RemoteException ignored) {
        } finally {
//            getClient().close();
        }



    }
}
