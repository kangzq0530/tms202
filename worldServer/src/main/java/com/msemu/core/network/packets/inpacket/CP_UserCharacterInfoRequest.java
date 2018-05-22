package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_CharacterInfo;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;

/**
 * Created by Weber on 2018/5/23.
 */
public class CP_UserCharacterInfoRequest extends InPacket<GameClient> {

    private int updateTick;
    private int charId;

    public CP_UserCharacterInfoRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        this.updateTick = decodeInt();
        this.charId = decodeInt();
    }

    @Override
    public void runImpl() {

        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();
        final Character target = field.getCharByID(charId);

        if(target != null)
            chr.write(new LP_CharacterInfo(target, target == chr));

    }
}
