package com.msemu.core.network.packets.in;

import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.login.client.Account;
import com.msemu.login.client.character.Character;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/19.
 */
public class UpdateCharacterSelectList extends InPacket<LoginClient> {

    private int changedCount;

    private List<Integer> charPosArray;

    public UpdateCharacterSelectList(short opcode) {
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

        List<Character> characters = account.getCharacters();


        for(int i = 0; i < charPosArray.size(); i++) {
            int charId = charPosArray.get(i);
            if (characters.get(i).getId() != charId) {
                characters.get(i).setCharacterPos(i);
            }
        }
        DatabaseFactory.getInstance().saveToDB(characters);
    }
}
