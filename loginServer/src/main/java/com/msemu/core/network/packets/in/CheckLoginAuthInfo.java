package com.msemu.core.network.packets.in;

import com.msemu.commons.enums.ClientState;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.commons.utils.BCryptUtils;
import com.msemu.commons.utils.HexUtils;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.Login.CheckPasswordResult;
import com.msemu.core.network.packets.out.Login.SetAccountGender;
import com.msemu.core.network.packets.out.Login.WorldInformation;
import com.msemu.core.network.packets.out.Login.WorldInformationEnd;
import com.msemu.login.LoginServer;
import com.msemu.login.client.Account;
import com.msemu.login.enums.LoginResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Weber on 2018/4/19.
 */
public class CheckLoginAuthInfo extends InPacket<LoginClient> {

    private static final Logger log = LoggerFactory.getLogger("Authentication");

    private String username;

    private String password;

    private String macAddress;

    public CheckLoginAuthInfo(short opcode) {
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
        LoginResultType status = LoginResultType.Nop;
        Account account = null;
        try {

            account = Account.findByUserName(username);
            if (account == null) {
                status = LoginResultType.AccountNotExists;
                log.warn("LoginStatus: [AccountNotExists] IP: {}, username: {}", getClient().getIP(), username);
            } else if (!BCryptUtils.checkPassword(password, account.getPassword())) {
                status = LoginResultType.InvalidPassword;
                log.warn("LoginStatus: [LoginSuccess] IP: {}, accountID: {}, username: {} can't kick out from WorldServer", getClient().getIP(), account.getId(), account.getUsername());
            } else {
                //TODO : Check Password, IP
                if (account.getPic().equals("")) {
                    getClient().write(new SetAccountGender(getClient()));
                    return;
                } else {
                    boolean isOnline = LoginServer.getRmi().isAccountOnline(account);
                    boolean result = LoginServer.getRmi().kickPlayerByAccount(account);
                    if (isOnline && !result) {
                        log.warn("LoginStatus: [ServerError] IP: {}, accountID: {}, username: {} can't kick out from WorldServer", getClient().getIP(), account.getId(), account.getUsername());
                        status = LoginResultType.ServerError;
                    } else {
                        log.warn("LoginStatus: [LoginSuccess] IP: {}, accountID: {}, username: {} is authenticated", getClient().getIP(), account.getId(), account.getUsername());
                        status = LoginResultType.LoginSuccess;
                    }
                }
            }
            if (!status.equals(LoginResultType.LoginSuccess)) {
                getClient().write(new CheckPasswordResult(null, status, null));
                return;
            }
            if (getClient().compareAndSetState(ClientState.CONNECTED, ClientState.AUTHED)) {
                getClient().setAccount(account);
                getClient().write(new CheckPasswordResult(account, status, null));
                List<WorldInfo> worlds = LoginServer.getRmi().getWorlds();
                worlds.forEach(world -> getClient().write(new WorldInformation(world)));
                getClient().write(new WorldInformationEnd());
            }
        } catch (Exception ignore) {
            status = LoginResultType.ServerError;
            log.error(String.format("LoginStatus: [Exception] IP: %s, ", getClient().getIP()), ignore);
        }
        getClient().write(new CheckPasswordResult(account, status, null));
    }


}
