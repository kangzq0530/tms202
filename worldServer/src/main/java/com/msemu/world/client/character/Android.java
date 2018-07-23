package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.android.LP_AndroidEnterField;
import com.msemu.core.network.packets.outpacket.user.android.LP_AndroidLeaveField;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.field.lifes.AbstractAnimatedFieldLife;
import com.msemu.world.enums.FieldObjectType;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Weber on 2018/5/22.
 */
public class Android extends AbstractAnimatedFieldLife {

    @Getter
    private Character owner;

    @Getter
    private int type;

    @Getter
    private AndroidInfo androidInfo = new AndroidInfo();


    @Override
    public FieldObjectType getFieldObjectType() {
        return FieldObjectType.ANDROID;
    }

    @Override
    public void enterScreen(GameClient client) {
        client.write(new LP_AndroidEnterField(this));
    }

    @Override
    public void outScreen(GameClient client) {
        client.write(new LP_AndroidLeaveField(this));
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getOwner().getId());
        outPacket.encodeByte(getType());
        outPacket.encodePosition(getPosition());
        outPacket.encodeByte(getAction());
        outPacket.encodeShort(getFh());
        getAndroidInfo().encode(outPacket);
        List<Item> equippedItems = getOwner().getEquippedInventory().getItems();
        equippedItems.sort(Comparator.comparingInt(Item::getBagIndex));
        equippedItems.forEach(item -> {
            Equip equip = (Equip) item;
            if (item.getItemId() >= 1200 && item.getId() < 1208) {
                outPacket.encodeInt(equip.getItemId());
                outPacket.encodeInt(equip.getOptions().get(6));
            }
        });
    }
}
