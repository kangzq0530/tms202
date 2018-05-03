package com.msemu.core.network.packets.in;

import com.msemu.commons.enums.ClientState;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.login.LP_CheckPasswordResult;
import com.msemu.login.LoginServer;
import com.msemu.login.client.Account;
import com.msemu.login.enums.LoginResultCode;
import com.msemu.login.service.LoginCookieService;
import com.msemu.login.service.relogin.ReLoginInfo;

/**
 * Created by Weber on 2018/5/2.
 */
public class CP_LogoutWorld extends InPacket<LoginClient> {
    public CP_LogoutWorld(short opcode) {
        super(opcode);
    }

    private String token;

    @Override
    public void read() {
        skip(2);
        token = decodeString();
    }

    @Override
    public void runImpl() {
        ReLoginInfo info = LoginCookieService.getInstance().getReLoginInfoByToken(token);

        if (info.getUsername() == null || info.getUsername().isEmpty()) {
            getClient().close();
            return;
        }

        Account account = Account.findByUserName(info.getUsername());

        if (account == null) {
            getClient().close();
            return;
        }
        if (getClient().compareAndSetState(ClientState.CONNECTED, ClientState.AUTHED)) {
            LoginServer.getRmi().kickPlayerByAccount(account);
            getClient().setAccount(account);
            getClient().setLoginResult(LoginResultCode.LoginSuccess);
            getClient().write(new LP_CheckPasswordResult(account, LoginResultCode.LoginSuccess, FileTime.getTimeFromLong(0)));
        } else {
            getClient().close();
        }
    }
}
