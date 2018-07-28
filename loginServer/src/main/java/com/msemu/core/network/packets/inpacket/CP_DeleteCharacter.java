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
