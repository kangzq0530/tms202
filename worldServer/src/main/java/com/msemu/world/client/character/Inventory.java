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

package com.msemu.world.client.character;

import com.msemu.commons.data.enums.InvType;
import com.msemu.commons.database.Schema;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.constants.ItemConstants;
import com.msemu.world.enums.BodyPart;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/13.
 */
@Schema
@Entity
@Table(name = "inventories")
@Getter
@Setter
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventoryId")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Item> items;
    @Column(name = "type")
    private InvType type;
    @Column(name = "slots")
    private byte slots;
    @Getter
    @Transient
    private final ReentrantLock lock = new ReentrantLock();

    public Inventory() {
        items = new ArrayList<>();
        type = InvType.EQUIP;
    }

    public Inventory(InvType t, int slots) {
        this.type = t;
        items = new ArrayList<>();
        this.slots = (byte) slots;
    }

    public int addItem(Item item) {
        if (getItems().size() <= getSlots() && !getItems().contains(item)) {
            getItems().add(item);
            item.setInventoryId(getId());
            item.setInvType(type);
            sortItemsByIndex();
            return item.getBagIndex();
        }
        return 0;
    }

    public void removeItem(Item item) {
        if (getItems().contains(item)) {
            item.setBagIndex(0);
            getItems().remove(item);
            item.setInvType(ItemConstants.getInvTypeFromItemID(item.getItemId()));
            item.setInventoryId(0);
            sortItemsByIndex();
        }
    }

    public int getFirstOpenSlot() {
        for (int i = 1; i <= getSlots(); i++) {
            if (getItemBySlot(i) == null) {
                return i;
            }
        }
        return 0;
    }

    public Item getFirstItemByBodyPart(BodyPart bodyPart) {
        List<Item> items = getItemsByBodyPart(bodyPart);
        return items != null && !items.isEmpty() ? items.get(0) : null;
    }

    public List<Item> getItemsByBodyPart(BodyPart bodyPart) {
        return getItems().stream().filter(item -> item.getBagIndex() == bodyPart.getValue()).collect(Collectors.toList());
    }


    public void sortItemsByIndex() {
        getItems().sort(Comparator.comparingInt(Item::getBagIndex));
    }


    public Item getItemBySlot(int bagIndex) {
        return getItems().stream().filter(item -> item.getBagIndex() == bagIndex).findAny().orElse(null);
    }

    public Item getItemByItemID(int itemId) {
        return getItems().stream().filter(item -> item.getItemId() == itemId).findFirst().orElse(null);
    }

    public boolean containsItem(int itemID) {
        return getItems().stream().anyMatch(item -> item.getItemId() == itemID);
    }

    public boolean canPickUp(Item item) {
        return !isFull() || (item.getInvType().isStackable() && getItemByItemID(item.getItemId()) != null);
    }

    private boolean isFull() {
        return getItems().size() >= getSlots();
    }

    public void doLock(Runnable runnable) {
        doLock(runnable, false);
    }
    public void doLock(Runnable runnable, boolean skipLock) {
        if(!skipLock) {
            getLock().lock();
            try {
                runnable.run();
            } finally {
                getLock().unlock();
            }
        } else {
            runnable.run();
        }
    }

    public void clear() {
        getItems().clear();
    }
}
