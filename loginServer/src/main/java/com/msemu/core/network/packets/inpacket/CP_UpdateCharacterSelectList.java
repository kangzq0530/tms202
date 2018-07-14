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
