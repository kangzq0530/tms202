package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.outpacket.login.LP_CheckDuplicatedIDResult;
import com.msemu.login.client.character.Character;
import com.msemu.login.data.StringData;

/**
 * Created by Weber on 2018/4/19.
 */
public class CP_CheckDuplicatedID extends InPacket<LoginClient> {

    private String charName;

    public CP_CheckDuplicatedID(short opcode) {
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
        client.write(new LP_CheckDuplicatedIDResult(charName, used));
    }
}
