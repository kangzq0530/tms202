package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.data.enums.InvType;
import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.remote.LP_UserSetActiveEffectItem;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.field.Field;
import com.msemu.world.data.ItemData;

/**
 * Created by Weber on 2018/5/22.
 */
public class CP_UserActivateEffectItem extends InPacket<GameClient> {

    private int effectItemId;

    public CP_UserActivateEffectItem(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        this.effectItemId = decodeInt();
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();
        final ItemTemplate template = ItemData.getInstance().getItemInfo(effectItemId);
        final InvType invType = template.getInvType();
        final int itemCate = effectItemId / 10000;
        final Item usedItem = chr.getInventoryByType(invType).getItemByItemID(effectItemId);

        //TODO checkItem valid
        if (invType == InvType.ETC) {
            if (itemCate != 429) {
                chr.enableActions();
                return;
            }
        } else if (invType != InvType.CASH || usedItem == null) {
            chr.enableActions();
            return;
        }

        chr.setActiveEffectItemID(effectItemId);

        field.broadcastPacket(new LP_UserSetActiveEffectItem(chr, effectItemId), chr);
    }
}
