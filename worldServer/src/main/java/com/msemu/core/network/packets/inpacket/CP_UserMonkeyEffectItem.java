package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.remote.LP_UserSetActiveEffectItem;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;

/**
 * Created by Weber on 2018/5/22.
 */
public class CP_UserMonkeyEffectItem extends InPacket<GameClient> {

    private int monkeyEffectItemId;

    public CP_UserMonkeyEffectItem(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        this.monkeyEffectItemId = decodeInt();
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();

        if(monkeyEffectItemId == 3994361 || monkeyEffectItemId == 3994359) {
            chr.setMonkeyEffectItemID(monkeyEffectItemId);
            field.broadcastPacket(new LP_UserSetActiveEffectItem(chr, monkeyEffectItemId), chr);
        }

    }
}
