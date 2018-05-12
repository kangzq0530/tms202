package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.NpcMessageType;

/**
 * Created by Weber on 2018/5/2.
 */
public class CP_UserScriptMessageAnswer extends InPacket<GameClient> {

    private NpcMessageType lastMessageType;

    private byte action = -1;

    private int selection = -1;

    private String text;

    public CP_UserScriptMessageAnswer(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        lastMessageType = NpcMessageType.getByValue(decodeByte());

        if( NpcMessageType.NM_ASK_AVATAR_EX.equals(lastMessageType) && available() >= 4) {
            decodeShort();
        }

        if(available() > 0) {
            action = decodeByte();
        }

        if(NpcMessageType.NM_ASK_TEXT.equals(lastMessageType)) {
            if(action != 0) {
                text = decodeString();
            }
        } else if (available() >= 4) {
            selection = decodeInt();
        } else if (available() > 0) {
            selection = decodeByte();
        }

        Character chr = getClient().getCharacter();
        chr.getScriptManager().handleAction(lastMessageType, action, selection);
    }

    @Override
    public void runImpl() {

    }
}
