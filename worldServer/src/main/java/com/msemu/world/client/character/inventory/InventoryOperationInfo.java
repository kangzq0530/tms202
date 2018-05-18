package com.msemu.world.client.character.inventory;

import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.enums.InventoryOperationType;
import lombok.Getter;

import java.lang.ref.WeakReference;

/**
 * Created by Weber on 2018/4/28.
 */

public class InventoryOperationInfo {
    @Getter
    private final InventoryOperationType type;
    private final WeakReference<Item> item;
    @Getter
    private final int oldBagIndex;
    @Getter
    private final int oldQuantity;

    public InventoryOperationInfo(InventoryOperationType type, Item item, int oldBagIndex, int oldQuantity) {
        this.type = type;
        this.item = new WeakReference<>(item);
        this.oldBagIndex = oldBagIndex;
        this.oldQuantity = oldQuantity;
    }

    public InventoryOperationInfo(InventoryOperationType type, Item item, int oldBagIndex) {
        this(type, item, oldBagIndex, item.getQuantity());
    }

    public Item getItem() {
        return item.get();
    }


    public void clear() {
        item.clear();
    }
}
