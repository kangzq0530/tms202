package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.skill.TemporaryStatManager;

/**
 * Created by Weber on 2018/5/23.
 */
public class CP_UserStatChangeItemCancelRequest extends InPacket<GameClient> {

    private int itemId;

    public CP_UserStatChangeItemCancelRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        this.itemId = decodeInt();
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final TemporaryStatManager tsm = chr.getTemporaryStatManager();
        tsm.removeStatsBySkill(-itemId);
    }
}
