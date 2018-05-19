package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.Rand;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.Stat;

/**
 * Created by Weber on 2018/5/12.
 */
public class CP_UserAbilityUpRequest extends InPacket<GameClient> {

    private int updateTick;
    private int statVal;

    public CP_UserAbilityUpRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        updateTick = decodeInt();
        statVal = decodeInt();
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        Stat stat = Stat.getByVal(statVal);
        int amount = 1;
        if(stat == null || chr.getStat(Stat.AP) <= 0)
            return;
        if(stat == Stat.MAX_HP || stat == Stat.MAX_MP) {
            // TODO 不同職業增加不同數量
            amount = Rand.get(10, 50);
        }
        chr.addStat(stat, amount);
    }
}
