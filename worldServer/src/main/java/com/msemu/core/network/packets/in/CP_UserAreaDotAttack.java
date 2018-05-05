package com.msemu.core.network.packets.in;

import com.msemu.commons.enums.InHeader;
import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.AttackInfo;

/**
 * Created by Weber on 2018/5/4.
 */
public class CP_UserAreaDotAttack extends InPacket<GameClient> {

    private AttackInfo attackInfo;

    public CP_UserAreaDotAttack(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        attackInfo = new AttackInfo();
        attackInfo.isDotAttack = true;

    }

    @Override
    public void runImpl() {
    }
}
