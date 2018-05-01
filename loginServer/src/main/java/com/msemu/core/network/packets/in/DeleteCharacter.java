package com.msemu.core.network.packets.in;

import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.BCryptUtils;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.login.DeleteCharacterResult;
import com.msemu.login.client.Account;
import com.msemu.login.client.character.Character;
import com.msemu.login.enums.LoginResultType;

/**
 * Created by Weber on 2018/4/22.
 */
public class DeleteCharacter extends InPacket<LoginClient> {

    private String secondPassowrd = "";

    private int charId = -1;

    public DeleteCharacter(short opcode) {
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

        if(!BCryptUtils.checkPassword(secondPassowrd, account.getPic())) {
            getClient().write(new DeleteCharacterResult(0, LoginResultType.InvalidSecondPassword));
            return;
        }

        Character chr = account.getCharacters().stream().filter(c -> c.getId() == charId)
                .findFirst().orElse(null);

        if (chr != null) {
            Character dbChar = Character.getById(chr.getId());
            DatabaseFactory.getInstance().deleteFromDB(dbChar);
            account.getCharacters().remove(chr);
            getClient().write(new DeleteCharacterResult(chr.getId(), LoginResultType.LoginSuccess));
        } else {
            getClient().write(new DeleteCharacterResult(0, LoginResultType.ServerError));
        }
    }
}
