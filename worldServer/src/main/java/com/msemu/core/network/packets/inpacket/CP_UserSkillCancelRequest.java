package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.skill.TemporaryStatManager;

/**
 * Created by Weber on 2018/5/19.
 */
public class CP_UserSkillCancelRequest extends InPacket<GameClient> {

    private int rReason;
    private int flags[] = new int[18];

    public CP_UserSkillCancelRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        rReason = decodeInt();
        decodeByte();
        for (int i = 0; i < 8; i++) {
            flags[i] = decodeInt();
        }
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();

        int[] _flags = tsm.getCurrentFlags();

        tsm.removeStatsBySkill(rReason);

        boolean check = true;
        for (int i = 0; i < 8; i++) {
            check =  flags[i] == _flags[i];
            if(!check)
                break;
        }

    }
}
