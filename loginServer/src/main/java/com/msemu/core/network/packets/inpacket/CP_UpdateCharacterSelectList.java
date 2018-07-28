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
import com.msemu.core.network.LoginClient;
import com.msemu.login.client.Account;
import com.msemu.login.client.character.Character;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Weber on 2018/4/19.
 */
public class CP_UpdateCharacterSelectList extends InPacket<LoginClient> {

    private int changedCount;

    private List<Integer> charPosArray;

    public CP_UpdateCharacterSelectList(short opcode) {
        super(opcode);
        charPosArray = new ArrayList<>();
    }

    @Override
    public void read() {

        decodeInt();
        decodeByte();

        changedCount = decodeInt();
        for(int i = 0 ; i < changedCount; i++) {
            charPosArray.add(decodeInt());
        }
    }

    @Override
    public void runImpl() {

        Account account = client.getAccount();

        int realCharCount = account.getCharacters().size();

        if(realCharCount != changedCount) {
            return;
        }

        Set<Character> characters = account.getCharacters();


        for(int i = 0; i < charPosArray.size(); i++) {
            int charId = charPosArray.get(i);
            for (Character character : characters) {
                if (character.getId() != charId) {
                    character.setCharacterPos(i);
                }
            }
        }
        DatabaseFactory.getInstance().saveToDB(characters);
    }
}
