package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.lifes.Npc;
import com.msemu.world.enums.ScriptType;

/**
 * Created by Weber on 2018/5/1.
 */
public class CP_UserSelectNpc extends InPacket<GameClient> {

    private int objectID;

    public CP_UserSelectNpc(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        objectID = decodeInt();
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        Npc npc = chr.getField().getNpcByObjectID(objectID);
        if (npc == null)
            return;
        System.out.println("Npc: " + npc.getTemplate().toString());
        if(!npc.getScript().isEmpty()) {
            chr.getScriptManager().startScript(npc.getTemplateId(), npc.getScript(), ScriptType.NPC);
        } else {
            chr.getScriptManager().startScript(npc.getTemplateId(), String.valueOf(npc.getTemplateId()), ScriptType.NPC);
        }
    }
}
