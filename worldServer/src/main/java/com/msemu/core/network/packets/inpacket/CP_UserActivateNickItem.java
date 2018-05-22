package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.remote.LP_UserSetActiveNickItem;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.data.ItemData;

/**
 * Created by Weber on 2018/5/23.
 */
public class CP_UserActivateNickItem extends InPacket<GameClient> {

    private int nickItemID;
    private int slotPos;

    public CP_UserActivateNickItem(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();
        final ItemTemplate itemInfo = ItemData.getInstance().getItemInfo(nickItemID);

        final boolean hasItem = chr.getInventoryByType(itemInfo.getInvType())
                .containsItem(nickItemID);

        if(!hasItem)
            return;
        chr.setActiveNickItemID(nickItemID);
        field.broadcastPacket(new LP_UserSetActiveNickItem(chr, nickItemID, ""), chr);


    }
}
