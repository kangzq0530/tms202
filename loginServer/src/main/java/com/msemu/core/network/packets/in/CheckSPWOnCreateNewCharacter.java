package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.BCryptUtils;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.Login.CheckSPWOnCreateNewCharacterResult;
import com.msemu.login.client.Account;
import com.msemu.login.enums.LoginResultType;

/**
 * Created by Weber on 2018/4/19.
 */
public class CheckSPWOnCreateNewCharacter extends InPacket<LoginClient> {

    private String secondPassword = "";

    public CheckSPWOnCreateNewCharacter(short opcode) {
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

        // TODO 這邊應該要有個FLAG確認已經正確輸入第二組密碼，才能創角色/刪角色

        if (checkSPW||true) {
            client.write(new CheckSPWOnCreateNewCharacterResult(LoginResultType.LoginSuccess));
        } else {
            client.write(new CheckSPWOnCreateNewCharacterResult(LoginResultType.InvalidSecondPassword));
        }

    }
}
