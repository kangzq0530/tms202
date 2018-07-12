package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyOperation;

/**
 * Created by Weber on 2018/5/4.
 */
public class JoinPartyDone2Response implements IPartyResult {

    private int playerCountInCafeBonus = 0;

    public JoinPartyDone2Response(int playerCountInCafeBonus) {
        this.playerCountInCafeBonus = playerCountInCafeBonus;
    }

    @Override
    public PartyOperation getType() {
        return PartyOperation.PartyRes_JoinParty_Done2;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        //組隊員中網咖玩家是 %d名。同頻道裡打獵時可獲得網咖摯友優惠。
        outPacket.encodeByte(playerCountInCafeBonus > 0);
        outPacket.encodeInt(playerCountInCafeBonus);
    }
}
