package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.Login.CheckDuplicatedIDResult;
import com.msemu.login.client.character.Character;
import com.msemu.login.data.StringData;

/**
 * Created by Weber on 2018/4/19.
 */
public class CheckDuplicatedID extends InPacket<LoginClient> {

    private String charName;

    public CheckDuplicatedID(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        charName = decodeString();
    }

    @Override
    public void runImpl() {

        boolean used = Character.isNameExists(charName) ||
                StringData.getInstance().getForbiddenName().getNames().contains(charName);
        client.write(new CheckDuplicatedIDResult(charName, used));
    }
}
