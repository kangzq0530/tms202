package com.msemu.core.network.packets.in;

import com.msemu.commons.enums.ClientState;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.login.CheckPasswordResult;
import com.msemu.login.client.Account;
import com.msemu.login.enums.LoginResultType;
import com.msemu.login.service.LoginCookieService;

/**
 * Created by Weber on 2018/5/2.
 */
public class LogoutWorld extends InPacket<LoginClient> {
    public LogoutWorld(short opcode) {
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
        String userName = LoginCookieService.getInstance().retriveUsernameByToken(token);

        if(userName == null || userName.isEmpty())
            return;

        Account account = Account.findByUserName(userName);

        if(account == null)
            return;
        if(getClient().compareAndSetState(ClientState.CONNECTED, ClientState.AUTHED)) {
            getClient().setAccount(account);
            getClient().setLoginResult(LoginResultType.LoginSuccess);
            getClient().write(new CheckPasswordResult(account, LoginResultType.LoginSuccess, FileTime.getTimeFromLong(0)));
        } else {
            getClient().close();
        }
    }
}
