package com.msemu.core.network.packets.outpacket.user.remote;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.AttackInfo;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/5/4.
 */
public class LP_UserMeleeAttack extends OutPacket<GameClient> {

    public LP_UserMeleeAttack(Character chr, AttackInfo ai) {
        super(OutHeader.LP_UserMeleeAttack);
        ai.encode(this, chr);
    }
}
