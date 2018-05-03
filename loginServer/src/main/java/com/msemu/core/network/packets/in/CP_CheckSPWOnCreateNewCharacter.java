package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.BCryptUtils;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.login.LP_CheckSPWOnCreateNewCharacterResult;
import com.msemu.login.client.Account;
import com.msemu.login.enums.LoginResultCode;

/**
 * Created by Weber on 2018/4/19.
 */
public class CP_CheckSPWOnCreateNewCharacter extends InPacket<LoginClient> {

    private String secondPassword = "";

    public CP_CheckSPWOnCreateNewCharacter(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        secondPassword = decodeString();
    }

    @Override
    public void runImpl() {

        Account account = client.getAccount();

        boolean checkSPW = BCryptUtils.checkPassword(secondPassword, account.getPic());

        client.setLoginResult(LoginResultCode.LoginSuccess);
        if (checkSPW||true) {
            client.write(new LP_CheckSPWOnCreateNewCharacterResult(getClient().getLoginResult()));
        } else {
            getClient().setLoginResult(LoginResultCode.IncorrectSPW);
            client.write(new LP_CheckSPWOnCreateNewCharacterResult(getClient().getLoginResult()));
        }

    }
}
