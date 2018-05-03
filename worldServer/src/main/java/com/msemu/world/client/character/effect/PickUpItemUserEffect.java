package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.enums.UserEffectType;

import java.lang.ref.WeakReference;

/**
 * Created by Weber on 2018/5/3.
 */
public class PickUpItemUserEffect implements IUserEffect {

    private final WeakReference<Item> item;

    public PickUpItemUserEffect(Item item) {
        this.item = new WeakReference<>(item);
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.PickUpItem;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        item.get().encode(outPacket);
    }
}
