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
        LoginResultCode status;
        Account account = null;
        try {
            account = Account.findByUserName(username);
            if (account == null) {
                status = LoginResultCode.NotRegistered;
                log.warn("LoginStatus: [NotRegistered] IP: {}, username: {}", getClient().getIP(), username);
            } else if (!BCryptUtils.checkPassword(password, account.getPassword())) {
                status = LoginResultCode.IncorrectPassword;
                log.warn("LoginStatus: [LoginSuccess] IP: {}, accountID: {}, username: {} can't kick outpacket from WorldServer", getClient().getIP(), account.getId(), account.getUsername());
            } else {
                //TODO : Check Password, IP
                if (account.getPic().equals("")) {
                    getClient().write(new LP_SetAccountGender(getClient()));
                    return;
                } else {
                    boolean isOnline = LoginServer.getRmi().isAccountOnline(account);
                    boolean result = LoginServer.getRmi().kickPlayerByAccount(account);
                    if (isOnline && !result) {
                        log.warn("LoginStatus: [DBFail] IP: {}, accountID: {}, username: {} can't kick outpacket from WorldServer", getClient().getIP(), account.getId(), account.getUsername());
                        status = LoginResultCode.DBFail;
                    } else {
                        log.warn("LoginStatus: [LoginSuccess] IP: {}, accountID: {}, username: {} is authenticated", getClient().getIP(), account.getId(), account.getUsername());
                        status = LoginResultCode.LoginSuccess;
                    }
                }
            }
            if (!status.equals(LoginResultCode.LoginSuccess)) {
                getClient().write(new LP_CheckPasswordResult(null, status, null));
                return;
            }
            if (getClient().compareAndSetState(ClientState.CONNECTED, ClientState.AUTHED)) {
                getClient().setAccount(account);
                getClient().write(new LP_CheckPasswordResult(account, status, null));
                List<WorldInfo> worlds = LoginServer.getRmi().getWorlds();
                worlds.forEach(world -> getClient().write(new LP_WorldInformation(world)));
                getClient().write(new LP_WorldInformationEnd());
            }
        } catch (Exception ignore) {
            status = LoginResultCode.DBFail;
            log.error(String.format("LoginStatus: [Exception] IP: %s, ", getClient().getIP()), ignore);
        }
        getClient().write(new LP_CheckPasswordResult(account, status, null));
    }


}
