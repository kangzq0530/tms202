/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.enums.ClientState;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.outpacket.login.LP_CheckPasswordResult;
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
