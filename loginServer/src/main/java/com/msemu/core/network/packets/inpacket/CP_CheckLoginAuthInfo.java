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
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.commons.utils.BCryptUtils;
import com.msemu.commons.utils.HexUtils;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.outpacket.login.LP_CheckPasswordResult;
import com.msemu.core.network.packets.outpacket.login.LP_SetAccountGender;
import com.msemu.core.network.packets.outpacket.login.LP_WorldInformation;
import com.msemu.core.network.packets.outpacket.login.LP_WorldInformationEnd;
import com.msemu.login.LoginServer;
import com.msemu.login.client.Account;
import com.msemu.login.enums.LoginResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Weber on 2018/4/19.
 */
public class CP_CheckLoginAuthInfo extends InPacket<LoginClient> {

    private static final Logger log = LoggerFactory.getLogger("Authentication");

    private String username;

    private String password;

    private String macAddress;

    public CP_CheckLoginAuthInfo(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

        macAddress = HexUtils.byteArraytoHex(decodeBytes(6));
        skip(16);
        username = decodeString();
        password = decodeString();
    }

    @Override
    public void runImpl() {
        final LoginClient client = getClient();
        LoginResultCode status = LoginResultCode.SystemErr;
        Account account = null;
        try {
            account = Account.findByUserName(username);
            if (account == null) {
                status = LoginResultCode.NotRegistered;
                log.warn("LoginStatus: [NotRegistered] IP: {}, username: {}", getClient().getIP(), username);
            } else if (!BCryptUtils.checkPassword(password, account.getPassword())) {
                status = LoginResultCode.IncorrectPassword;
                client.setAccount(null);
                log.warn("LoginStatus: [LoginSuccess] IP: {}, accountID: {}, username: {} can't kick out from WorldServer", getClient().getIP(), account.getId(), account.getUsername());
            } else {
                //TODO : Check Password, IP
                boolean isOnline = LoginServer.getRmi().isAccountOnline(account);
                boolean result = LoginServer.getRmi().kickPlayerByAccount(account);
                if (isOnline && !result) {
                    log.warn("LoginStatus: [DBFail] IP: {}, accountID: {}, username: {} can't kick out from WorldServer", getClient().getIP(), account.getId(), account.getUsername());
                    status = LoginResultCode.DBFail;
                } else if (account.getPic() == null || account.getPic().equals("")) {
                    client.compareAndSetState(ClientState.CONNECTED, ClientState.AUTHED_GG);
                    client.setAccount(account);
                    client.write(new LP_SetAccountGender());
                    return;
                } else if (client.compareAndSetState(ClientState.CONNECTED, ClientState.AUTHED)) {
                    final List<WorldInfo> worlds = LoginServer.getRmi().getWorlds();
                    status = LoginResultCode.LoginSuccess;
                    client.setAccount(account);
                    client.write(new LP_CheckPasswordResult(account, status, null));
                    worlds.forEach(world -> client.write(new LP_WorldInformation(world)));
                    client.write(new LP_WorldInformationEnd());
                    log.warn("LoginStatus: [LoginSuccess] IP: {}, accountID: {}, username: {} is authenticated", client.getIP(), account.getId(), account.getUsername());
                }
            }
        } catch (Exception loginException) {
            status = LoginResultCode.DBFail;
            log.error(String.format("LoginStatus: [Exception] IP: %s, ", client.getIP()), loginException);
        }
        if (!status.equals(LoginResultCode.LoginSuccess)) {
            client.write(new LP_CheckPasswordResult(null, status, null));
        } else {
            client.write(new LP_CheckPasswordResult(account, status, null));
        }
    }
}
