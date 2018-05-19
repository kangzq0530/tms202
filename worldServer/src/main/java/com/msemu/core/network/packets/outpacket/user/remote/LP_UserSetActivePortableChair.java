package com.msemu.core.network.packets.outpacket.user.remote;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;

/**
 * Created by Weber on 2018/5/15.
 */
public class LP_UserSetActivePortableChair extends OutPacket<GameClient> {

    public LP_UserSetActivePortableChair(Character chr, Item chairItem, String chairString) {
        super(OutHeader.LP_UserSetActivePortableChair);
        encodeInt(chr.getId());
        encodeInt(chairItem.getItemId());
        boolean btextChir = chairString == null || chairString.isEmpty();
        encodeByte(btextChir);
        if(btextChir) {
            encodeString(chairString);
        }
        int towerChairSize = 0;
        for(int i = 0 ; i < towerChairSize; i++) {
            encodeInt(0);
        }
        boolean unk1 = false;
        encodeByte(unk1);
        if(unk1) {
            encodeInt(0);
            encodeInt(0);
        }
        int unk2 = 0;
        encodeInt(unk2);
        encodeByte(false);
        encodeInt(0);
        encodeInt(0);
    }
}
