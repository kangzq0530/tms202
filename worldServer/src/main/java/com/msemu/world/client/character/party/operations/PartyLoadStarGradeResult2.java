package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/5/4.
 */
public class PartyLoadStarGradeResult2 extends PartyLoadStarGradeResult {

    private int removeAll;

    public PartyLoadStarGradeResult2(int removeAll) {
        this.removeAll = removeAll;
    }

    @Override
    public PartyResultType getType() {
        return PartyResultType.PartyRes_Load_StarGrade_Result2;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(removeAll);
        super.encode(outPacket);
    }
}
