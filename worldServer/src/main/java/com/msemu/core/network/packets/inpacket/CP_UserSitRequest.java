package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.common.LP_UserSitResult;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.constants.ItemConstants;

/**
 * Created by Weber on 2018/5/16.
 */
public class CP_UserSitRequest extends InPacket<GameClient> {

    private short otherChairPos;


    public CP_UserSitRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        otherChairPos = decodeShort();
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        if(otherChairPos > -1) {
            Item item = chr.getInstallInventory().getItemBySlot(otherChairPos);
            if(item == null || !ItemConstants.類型.椅子(item.getItemId())) {
                otherChairPos = -1;
            }
        }
        getClient().write(new LP_UserSitResult(chr, otherChairPos));
    }
}
