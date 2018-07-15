package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.BCryptUtils;
import com.msemu.core.configs.CoreConfig;
import com.msemu.core.configs.LoginConfig;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.outpacket.login.LP_DeleteCharacterResult;
import com.msemu.login.client.Account;
import com.msemu.login.client.character.Character;
import com.msemu.login.enums.LoginResultCode;

/**
 * Created by Weber on 2018/4/22.
 */
public class CP_DeleteCharacter extends InPacket<LoginClient> {

    private String secondPassowrd = "";

    private int charId = -1;

    public CP_DeleteCharacter(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        secondPassowrd = decodeString();
        charId = decodeInt();
    }

    @Override
    public void runImpl() {
        Account account = getClient().getAccount();

        if(!BCryptUtils.checkPassword(secondPassowrd, account.getPic()) && false) {
            getClient().write(new LP_DeleteCharacterResult(0, LoginResultCode.IncorrectSPW));
            return;
        }

        Character chr = account.getCharacters().stream().filter(c -> c.getId() == charId)
                .findFirst().orElse(null);

        if (chr != null) {
            Character dbChar = Character.getById(chr.getId());
            DatabaseFactory.getInstance().deleteFromDB(dbChar);
            account.getCharacters().remove(chr);
            getClient().write(new LP_DeleteCharacterResult(chr.getId(), LoginResultCode.LoginSuccess));
        } else {
            getClient().write(new LP_DeleteCharacterResult(0, LoginResultCode.DBFail));
        }
    }
}
