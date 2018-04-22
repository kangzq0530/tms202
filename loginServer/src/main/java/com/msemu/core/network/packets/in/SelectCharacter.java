package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.BCryptUtils;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.Login.SelectCharacterResult;
import com.msemu.login.client.Account;
import com.msemu.login.enums.LoginResultType;

/**
 * Created by Weber on 2018/4/22.
 */
public class SelectCharacter extends InPacket<LoginClient> {


    private String secondPassword;

    private int characterID;

    public SelectCharacter(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        secondPassword = decodeString();
        characterID = decodeInt();
    }

    @Override
    public void runImpl() {
        Account account = getClient().getAccount();
        if (!BCryptUtils.checkPassword(secondPassword, account.getPic())) {
            getClient().write(new SelectCharacterResult(LoginResultType.InvalidSecondPassword, new byte[]{0, 0, 0, 0}, (short) 0, 0));
            return;
        }
        getClient().write(new SelectCharacterResult(LoginResultType.InvalidSecondPassword, new byte[]{(byte)192, (byte)168, (byte)156, 1}, (short) 0, 0));
    }
}
