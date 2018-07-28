package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.runestones.RuneStoneManager;
import com.msemu.world.enums.RuneStoneType;

public class CP_RuneStoneUseReq extends InPacket<GameClient> {

    private int updateTick;
    private int typeValue;
    private RuneStoneType type;

    public CP_RuneStoneUseReq(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        updateTick = decodeInt();
        typeValue = decodeInt();
        type = RuneStoneType.getByValue(typeValue);
    }

    @Override
    public void runImpl() {

        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();
        final RuneStoneManager rsm = field.getRuneStoneManager();
        // TODO  確認BUFF冷卻時間
        rsm.requestRuneStone(chr, type);
    }
}
