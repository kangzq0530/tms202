/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.world.client.character.inventory;

import com.msemu.commons.data.enums.InvType;
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
    @Getter
    private final InvType oldInvType;

    public InventoryOperationInfo(InventoryOperationType type, Item item, InvType oldInvType, int oldBagIndex, int oldQuantity) {
        this.type = type;
        this.item = new WeakReference<>(item);
        this.oldBagIndex = oldBagIndex;
        this.oldQuantity = oldQuantity;
        this.oldInvType = oldInvType;
    }

    public InventoryOperationInfo(InventoryOperationType type, Item item, InvType oldInvType, int oldBagIndex) {
        this(type, item, oldInvType, oldBagIndex, item.getQuantity());
    }

    public Item getItem() {
        return item.get();
    }


    public void clear() {
        item.clear();
    }
}
