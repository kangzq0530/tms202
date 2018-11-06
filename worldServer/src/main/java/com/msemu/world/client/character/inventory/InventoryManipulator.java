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
import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_InventoryOperation;
import com.msemu.world.client.character.AvatarLook;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.Inventory;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.field.lifes.Drop;
import com.msemu.world.constants.ItemConstants;
import com.msemu.world.data.ItemData;
import com.msemu.world.enums.InventoryOperationType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.msemu.world.enums.InventoryOperationType.*;

/**
 * Created by Weber on 2018/4/28.
 */
public class InventoryManipulator {


    public static void unequipped(Character chr, short srcSlot, short destSlot) {
        AvatarLook al = chr.getAvatarData().getAvatarLook();
        List<InventoryOperationInfo> operates = new ArrayList<>();


        Equip srcEquip = (Equip) chr.getEquippedInventory()
                .getItemBySlot(-srcSlot);
        Equip destEquip = (Equip) chr.getEquipInventory()
                .getItemBySlot(destSlot);
        if (destSlot <= 0 || srcEquip == null || srcSlot == -55
                || (destEquip != null && srcSlot <= 0))
            return;
        chr.chatMessage(String.format("[卸下裝備] 來源: %d 道具: %s(%d)",
                srcSlot,
                srcEquip.getTemplate().getName(),
                srcEquip.getTemplate().getItemId()
                )
        );
        chr.getEquippedInventory().getLock().lock();
        chr.getEquipInventory().getLock().lock();
        try {
            chr.getEquippedInventory().removeItem(srcEquip);
            chr.getEquipInventory().addItem(srcEquip);
            srcEquip.setBagIndex(destSlot);
        } finally {
            chr.getEquippedInventory().getLock().unlock();
            chr.getEquipInventory().getLock().unlock();
        }
        if (ItemConstants.類型.武器(srcEquip.getItemId())) {
            chr.getAvatarData().getAvatarLook().setWeaponId(0);
        } else if (ItemConstants.類型.副手(srcEquip.getItemId())) {
            chr.getAvatarData().getAvatarLook().setSubWeaponId(0);
        }
        List<Integer> newHairEqups = chr.getEquippedInventory().getItems().stream()
                .map(Item::getItemId).collect(Collectors.toList());
        al.setHairEquips(newHairEqups);
        operates.add(new InventoryOperationInfo(InventoryOperationType.MOVE,
                srcEquip, InvType.EQUIPPED, srcSlot));
        chr.write(new LP_InventoryOperation(true, false, operates));
        chr.renewAvatarLook();
        chr.renewCharacterStats();
    }

    public static void equip(Character chr, short srcSlot, short destSlot) {
        AvatarLook al = chr.getAvatarData().getAvatarLook();
        List<InventoryOperationInfo> operates = new ArrayList<>();
        Equip srcEquip = (Equip) chr.getEquipInventory()
                .getItemBySlot(srcSlot);
        Equip destEquip = (Equip) chr.getEquippedInventory()
                .getItemBySlot(-destSlot);
        chr.chatMessage(String.format("[穿上裝備] 來源: %d 道具: %s(%d) 目標: %d 道具: %s(%d)",
                srcSlot,
                srcEquip != null ? srcEquip.getTemplate().getName() : "無",
                srcEquip != null ? srcEquip.getTemplate().getItemId() : 0,
                destSlot,
                destEquip != null ? destEquip.getTemplate().getName() : "無",
                destEquip != null ? destEquip.getTemplate().getItemId() : 0
        ));
        if (srcEquip == null || ItemConstants.類型.isHarvesting(srcEquip.getItemId())) {
            chr.enableActions();
            return;
        }
        if (destEquip != null) {
            short nextSlot = (short) chr.getEquipInventory().getFirstOpenSlot();
            if (nextSlot > 0)
                unequipped(chr, destSlot, nextSlot);
            else {
                chr.enableActions();
                return;
            }
        }
        chr.getEquippedInventory().getLock().lock();
        chr.getEquipInventory().getLock().lock();
        try {
            chr.getEquipInventory().removeItem(srcEquip);
            chr.getEquippedInventory().addItem(srcEquip);
            srcEquip.setBagIndex(destSlot);
        } finally {
            chr.getEquippedInventory().getLock().unlock();
            chr.getEquipInventory().getLock().unlock();
        }
        operates.add(new InventoryOperationInfo(InventoryOperationType.MOVE,
                srcEquip, InvType.EQUIP, srcSlot));
        List<Integer> newHairEqups = chr.getEquippedInventory().getItems().stream()
                .map(Item::getItemId).collect(Collectors.toList());
        al.setHairEquips(newHairEqups);
        if (ItemConstants.類型.武器(srcEquip.getItemId())) {
            chr.getAvatarData().getAvatarLook().setWeaponId(srcEquip.getItemId());
        } else if (ItemConstants.類型.副手(srcEquip.getItemId())) {
            chr.getAvatarData().getAvatarLook().setSubWeaponId(srcEquip.getItemId());
        }
        chr.write(new LP_InventoryOperation(true, false, operates));
        chr.renewAvatarLook();
        chr.renewCharacterStats();
    }

    public static void drop(Character chr, InvType invType, short srcSlot, short destSlot, short quantity) {
        Item srcItem = chr.getInventoryByType(invType).getItemBySlot(srcSlot);
        Drop drop;
        List<InventoryOperationInfo> operates = new ArrayList<>();
        if (!srcItem.getInvType().isStackable() || quantity >= srcItem.getQuantity()) {
            chr.getInventoryByType(invType).getLock().lock();
            try {
                chr.getInventoryByType(invType).removeItem(srcItem);
            } finally {
                chr.getInventoryByType(invType).getLock().unlock();
            }
            srcItem.drop();
            drop = new Drop(-1, srcItem);
            operates.add(new InventoryOperationInfo(InventoryOperationType.REMOVE,
                    srcItem, invType, srcSlot));
        } else {
            Item dropItem = ItemData.getInstance().createItem(srcItem.getItemId());
            dropItem.setQuantity(quantity);
            srcItem.removeQuantity(quantity);
            drop = new Drop(-1, dropItem);
            operates.add(new InventoryOperationInfo(InventoryOperationType.UPDATE,
                    srcItem, invType, srcSlot));
        }
        chr.chatMessage(String.format("[丟棄裝備] 來源: %d 道具: %s(%d)",
                srcSlot,
                drop.getItem().getTemplate().getName(),
                drop.getItem().getTemplate().getItemId()));

        ItemTemplate t = srcItem.getTemplate();
        if (t.isTradeBlock() || t.isQuest()) {
            chr.getField().dropFadeOut(drop, chr.getPosition());
        } else {
            chr.getField().drop(drop, chr.getPosition());
        }
        chr.write(new LP_InventoryOperation(true, false, operates));
        chr.renewBulletIDForAttack();
    }

    public static void move(Character chr, InvType invType, short srcSlot, short destSlot) {
        if (srcSlot < 0 || destSlot < 0 || srcSlot == destSlot || invType == InvType.EQUIPPED)
            return;
        Item srcItem = chr.getInventoryByType(invType).getItemBySlot(srcSlot);
        Item destItem = chr.getInventoryByType(invType).getItemBySlot(destSlot);
        boolean bag = false, switchSrcDst = false, bothBag = false;

        // TODO 放入/取出擴充背包( 椅子背包、卷軸背包 ... etc 位置貌似在 100 up
        if (destSlot > chr.getInventoryByType(invType).getSlots()) {
            chr.enableActions();
            return;
        }
        if (srcSlot > chr.getInventoryByType(invType).getSlots()) {
            chr.enableActions();
            return;
        }
        chr.chatMessage(String.format("[移動裝備] 來源: %d 道具: %s(%d) 目標: %d 道具:  %s(%d)",
                srcSlot,
                srcItem != null ? srcItem.getTemplate().getName() : "無",
                srcItem != null ? srcItem.getTemplate().getItemId() : 0,
                destSlot,
                destItem != null ? destItem.getTemplate().getName() : "無",
                destItem != null ? destItem.getTemplate().getItemId() : 0
        ));

        List<InventoryOperationInfo> operates = new ArrayList<>();

        chr.getInventoryByType(invType).getLock().lock();
        try {
            if (destItem != null) {
                destItem.setBagIndex(srcSlot);
            }
            if (srcItem != null)
                srcItem.setBagIndex(destSlot);
            chr.getInventoryByType(invType).sortItemsByIndex();
        } finally {
            chr.getInventoryByType(invType).getLock().unlock();
        }

        operates.add(new InventoryOperationInfo(InventoryOperationType.MOVE,
                srcItem, invType, srcSlot));

        chr.write(new LP_InventoryOperation(true, false, operates));
        chr.renewBulletIDForAttack();
    }

    public static void update(final Character chr, final InvType invType, final int pos) {
        final Item item = chr.getInventoryByType(invType).getItemBySlot(pos);
        if (item == null)
            return;
        List<InventoryOperationInfo> operates = new ArrayList<>();
        operates.add(new InventoryOperationInfo(InventoryOperationType.ADD, item, item.getInvType(), item.getBagIndex()));
        chr.write(new LP_InventoryOperation(true, false, operates));
        chr.renewBulletIDForAttack();
    }
    public static void consume(final Character chr, final int itemID, final int amount) {
        consume(chr, itemID, amount, false);
    }
    public static void consume(final Character chr, final int itemID, final int amount, boolean skipLock) {
        InvType invType = ItemConstants.getInvTypeFromItemID(itemID);
        Inventory inventory = chr.getInventoryByType(invType);
        inventory.doLock(() -> {
            int lastAmount = amount;
            Item item = chr.getInventoryByType(invType).getItemByItemID(itemID);
            while (item != null && lastAmount > 0) {
                if (item.getQuantity() > lastAmount) {
                    consume(chr, item, lastAmount, true);
                    lastAmount = 0;
                } else {
                    consume(chr, item, item.getQuantity(), true);
                    lastAmount -= item.getQuantity();
                }
                item = chr.getInventoryByType(invType).getItemByItemID(itemID);
            }
        }, skipLock);
    }

    public static void consume(final Character chr, final Item item) {
        consume(chr, item, 1, false);
    }

    public static void consume(final Character chr, final Item item, final int consumeCount) {
        consume(chr, item, consumeCount, false);
    }

    public static void consume(final Character chr, final Item item, final int consumeCount, boolean skipLock) {
        Inventory inventory = chr.getInventoryByType(item.getInvType());
        if (item.getQuantity() == consumeCount && !ItemConstants.類型.可充值道具(item.getItemId())) {
            inventory.doLock(() -> {
                item.setQuantity(0);
                inventory.removeItem(item);
            }, skipLock);
            int oldBagIndex = item.getBagIndex();
            chr.write(new LP_InventoryOperation(true, false,
                    REMOVE, item, oldBagIndex));
        } else {
            inventory.doLock(() -> item.setQuantity(item.getQuantity() - consumeCount), skipLock);
            chr.write(new LP_InventoryOperation(true, false,
                    UPDATE, item, item.getBagIndex()));
        }
        chr.renewBulletIDForAttack();
    }

    public static void add(final Character chr, final Item item) {
        add(chr, item, false);
    }

    public static void add(final Character chr, final Item item, boolean skipLock) {

        final InvType invType = item.getInvType();
        final Inventory inventory = chr.getInventoryByType(invType);
        final ItemTemplate template = item.getTemplate();

        inventory.doLock(() -> {
            if (invType != InvType.EQUIP) {
                final int slotMax = template.getSlotMax();

                if (!ItemConstants.類型.可充值道具(item.getItemId())) {
                    // 非裝備、充值道具 限制最大堆疊數量
                    final List<Item> existsItems = inventory.getItems()
                            .stream().filter(each -> each.getItemId() == item.getItemId()).collect(Collectors.toList());

                    int lastQuantity = item.getQuantity();
                    Iterator<Item> iterator = existsItems.iterator();
                    while (lastQuantity > 0) {
                        if (iterator.hasNext()) {
                            Item bagItem = iterator.next();
                            if (bagItem.getQuantity() < slotMax &&
                                    (bagItem.getOwner().equalsIgnoreCase("") || bagItem.getOwner().equals(chr.getName())) &&
                                    bagItem.getDateExpire().equal(FileTime.getFileTimeFromType(FileTime.Type.MAX_TIME))) {
                                short newQuantity = (short) Math.min(bagItem.getQuantity() + lastQuantity, slotMax);
                                lastQuantity -= (newQuantity - bagItem.getQuantity());
                                item.setQuantity(lastQuantity);
                                bagItem.setQuantity(newQuantity);
                                chr.write(new LP_InventoryOperation(true, false,
                                        UPDATE, bagItem, bagItem.getBagIndex()));
                            }
                        } else {
                            break;
                        }
                    }

                    while (lastQuantity > 0) {
                        final Item insertItem;
                        if (lastQuantity > slotMax) {
                            short newQuantity = (short) Math.min(lastQuantity, slotMax);
                            lastQuantity -= newQuantity;
                            item.setQuantity(lastQuantity);
                            insertItem = ItemData.getInstance().createItem(item.getItemId());
                            insertItem.setDateExpire(item.getDateExpire());
                            insertItem.setOwner(item.getOwner());
                            insertItem.setQuantity(newQuantity);
                        } else {
                            insertItem = item;
                            lastQuantity = 0;
                        }
                        insertItem.setBagIndex(inventory.getFirstOpenSlot());
                        inventory.addItem(insertItem);
                        List<InventoryOperationInfo> operates = new ArrayList<>();
                        operates.add(new InventoryOperationInfo(InventoryOperationType.ADD, insertItem, insertItem.getInvType(), insertItem.getBagIndex()));
                        chr.write(new LP_InventoryOperation(true, false, operates));
                    }

                } else {
                    item.setBagIndex(inventory.getFirstOpenSlot());
                    inventory.addItem(item);
                }
            } else {
                item.setQuantity(1);
                item.setBagIndex(inventory.getFirstOpenSlot());
                inventory.addItem(item);
                List<InventoryOperationInfo> operates = new ArrayList<>();
                operates.add(new InventoryOperationInfo(InventoryOperationType.ADD, item, item.getInvType(), item.getBagIndex()));
                chr.write(new LP_InventoryOperation(true, false, operates));
            }
            chr.renewBulletIDForAttack();
        }, skipLock);
    }

    public static void update(Character chr, Item item) {
        chr.write(new LP_InventoryOperation(true, false,
                ADD, item, item.getBagIndex()));
    }


    public static void remove(Character chr, Item item) {
        consume(chr, item, item.getQuantity());
    }


    public static boolean canHold(Character chr, int itemId, int quantity) {
        final InvType invType = ItemConstants.getInvTypeFromItemID(itemId);
        final Inventory inventory = chr.getInventoryByType(invType);
        final AtomicBoolean result = new AtomicBoolean(false);
        inventory.doLock(() -> {
            if (ItemConstants.isEquip(itemId)) {
                result.set(chr.getEquipInventory().getSlots() > chr.getEquipInventory().getItems().size());
            } else {
                ItemTemplate template = ItemData.getInstance().getItemInfo(itemId);
                Item existsItem = inventory.getItemByItemID(itemId);
                result.set((existsItem != null && existsItem.getQuantity() + 1 < template.getSlotMax())
                        || inventory.getSlots() > inventory.getItems().size());
            }
        });
        return result.get();

    }

}
