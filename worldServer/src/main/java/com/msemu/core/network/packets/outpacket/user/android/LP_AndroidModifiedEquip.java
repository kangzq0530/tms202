package com.msemu.core.network.packets.outpacket.user.android;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Android;
import com.msemu.world.client.character.inventory.items.Equip;

/**
 * Created by Weber on 2018/5/22.
 */
public class LP_AndroidModifiedEquip extends OutPacket<GameClient> {

    public LP_AndroidModifiedEquip(Android android, Equip equip) {
        super(OutHeader.LP_AndroidModified);
        encodeInt(android.getOwner().getId());
        encodeByte(1 << equip.getBagIndex() - 1200);
        encodeInt(equip.getItemId());
        encodeInt(equip.getOptionBase(6));
        encodeInt(0);
    }
}
