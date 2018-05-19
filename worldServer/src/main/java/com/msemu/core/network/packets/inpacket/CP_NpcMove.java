package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.npc.LP_NpcMove;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.Npc;

/**
 * Created by Weber on 2018/5/3.
 */
public class CP_NpcMove extends InPacket<GameClient> {

    private int objectID;

    public CP_NpcMove(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        if(available() < 10)
            return;
        objectID = decodeInt();
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        Field field = chr.getField();
        Npc npc = field.getNpcByObjectID(objectID);
        if(npc != null) {
            chr.write(new LP_NpcMove(objectID, this));
        }
    }
}
