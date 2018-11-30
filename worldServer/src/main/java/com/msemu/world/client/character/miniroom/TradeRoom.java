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

package com.msemu.world.client.character.miniroom;

import com.msemu.commons.data.enums.InvType;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.miniroom.LP_MiniRoom;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.Inventory;
import com.msemu.world.client.character.inventory.InventoryManipulator;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.miniroom.actions.*;
import com.msemu.world.constants.GameConstants;
import com.msemu.world.constants.ItemConstants;
import com.msemu.world.data.ItemData;
import com.msemu.world.enums.FieldObjectType;
import com.msemu.world.enums.MiniGameMessageType;
import com.msemu.world.enums.MiniRoomLeaveResult;
import com.msemu.world.enums.MiniRoomType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TradeRoom extends MiniRoom {

    @Getter
    private AtomicLong creatorMoney = new AtomicLong(0);
    @Getter
    private AtomicLong guestMoney = new AtomicLong(0);

    @Getter
    private AtomicBoolean creatorConfirmed = new AtomicBoolean(false);
    @Getter
    private AtomicBoolean guestConfirmed = new AtomicBoolean(false);

    @Getter
    private Inventory creatorInventory = new Inventory(InvType.TRADE, 9);

    @Getter
    private Inventory guestInventory = new Inventory(InvType.TRADE, 9);

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private MiniRoomLeaveResult result = MiniRoomLeaveResult.MR_Closed;

    public TradeRoom() {

    }


    @Override
    public MiniRoomType getType() {
        return MiniRoomType.MR_TradingRoom;
    }

    @Override
    public int getMaxUsers() {
        return 2;
    }

    @Override
    public void create(Character creator) {
        addVisitor(creator);
        setClosed(false);
    }

    @Override
    public void enter(Character chr) {
        addVisitor(chr);
        if (getVisitors().size() == 2 && !isClosed()) {
            broadcast(new LP_MiniRoom(new UserChatAction(2, "系統訊息 : 進行楓幣交換請注意手續費")));
            broadcast(new LP_MiniRoom(new UserChatAction(2, "系統訊息 : 使用指令 '/幫助' 可以看到交易可用的指令說明.")));
            setClosed(false);
        } else {
            this.close();
        }
    }

    @Override
    public void leave(Character chr) {
        setResult(MiniRoomLeaveResult.MR_Closed);
        close();
    }

    @Override
    public void close() {
        // TODO 交易結果ㄉ訊息
        getVisitors().forEach(this::giveBackItems);
        setClosed(true);
        getVisitors().forEach(visitor -> {
            removeVisitor(visitor);
            visitor.write(new LP_MiniRoom(new MiniRoomLeaveAction(getResult(), visitor.getCharIndex(), visitor.getCharacter().getName())));
        });

    }

    public boolean canPutItem(Character chr) {
        final MiniRoomVisitor visitor = getVisitorByCharacter(chr);
        final AtomicBoolean confirmed = getConfirmedByVisitor(visitor);
        final boolean locked = confirmed == null || confirmed.get();
        final Inventory inventory = getInventoryByVisitor(visitor);
        return getVisitors().size() == 2 && !locked && (inventory != null && inventory.getItems().size() < 9);
    }

    public void putItem(Character chr, Item item, int quantity) {
        MiniRoomVisitor visitor = getVisitorByCharacter(chr);
        Inventory inventory = getInventoryByVisitor(visitor);
        if (inventory == null)
            return;
        if (ItemConstants.類型.可充值道具(item.getItemId()) || item.getQuantity() == quantity) {
            InventoryManipulator.consume(chr, item);
            item.setQuantity(quantity);
            item.setBagIndex(inventory.getFirstOpenSlot());
            inventory.addItem(item);
        } else {
            final Item newBundle = ItemData.getInstance().createItem(item.getItemId());
            InventoryManipulator.consume(chr, item, quantity);
            newBundle.setQuantity(quantity);
            newBundle.setBagIndex(inventory.getFirstOpenSlot());
            inventory.addItem(newBundle);
        }
        getVisitor(0).write(new LP_MiniRoom(new TradePutItemAction((visitor.equals(getVisitor(0)) ? 0 : 1), item)));
        getVisitor(1).write(new LP_MiniRoom(new TradePutItemAction((visitor.equals(getVisitor(1)) ? 0 : 1), item)));
    }

    public void putMoney(Character chr, long money) {
        final MiniRoomVisitor visitor = getVisitorByCharacter(chr);

        final AtomicLong _money = getMoneyByVisitor(visitor);
        if (_money == null)
            return;
        chr.addMoney(-money, false);
        _money.addAndGet(money);
        // 客戶端哪格垃圾寫ㄉ 照index不就好了 耖你媽
        getVisitor(0).write(new LP_MiniRoom(new TradePutMoneyAction((visitor.equals(getVisitor(0)) ? 0 : 1), money)));
        getVisitor(1).write(new LP_MiniRoom(new TradePutMoneyAction((visitor.equals(getVisitor(1)) ? 0 : 1), money)));
    }

    public void completeTrade(Character chr) {

        final MiniRoomVisitor visitor = getVisitorByCharacter(chr);
        final AtomicBoolean confirmed = getConfirmedByVisitor(visitor);

        if (confirmed == null || confirmed.get())
            return;

        confirmed.set(true);

        final boolean completed = creatorConfirmed.get() && guestConfirmed.get();

        int partnerIndex = getVisitor(0).equals(visitor) ? 1 : 0;

        getVisitor(partnerIndex).write(new LP_MiniRoom(new TradeConfirmedAction()));

        if (completed) {
            boolean checkCreator = checkSpace(getVisitor(0), guestInventory.getItems());
            boolean checkGuest = checkSpace(getVisitor(1), creatorInventory.getItems());
            boolean sameField = getVisitor(0).getCharacter().getField().equals(
                    getVisitor(1).getCharacter().getField());
            if (!sameField) {
                setResult(MiniRoomLeaveResult.TR_FieldError);
            } else if (!checkCreator || !checkGuest) {
                setResult(MiniRoomLeaveResult.TR_TradeFail);
            } else {
                setResult(MiniRoomLeaveResult.TR_TradeDone);
                exchangeItems();
            }
            close();
        }
    }

    @Override
    public FieldObjectType getFieldObjectType() {
        return FieldObjectType.TRADE;
    }

    @Override
    public void enterScreen(GameClient client) {
    }

    @Override
    public void outScreen(GameClient client) {
    }

    private void giveBackItems(MiniRoomVisitor visitor) {
        final Inventory inventory = getInventoryByVisitor(visitor);
        final AtomicLong money = getMoneyByVisitor(visitor);
        if (inventory != null) {
            inventory.getItems().forEach(item -> {
                InventoryManipulator.add(visitor.getCharacter(), item);
            });
            inventory.clear();
        }
        if (money != null) {
            visitor.getCharacter().addMoney(money.getAndSet(0), false);
        }
    }

    private void exchangeItems() {
        final MiniRoomVisitor creator = getVisitor(0);
        final MiniRoomVisitor guest = getVisitor(1);
        creatorInventory.getItems()
                .forEach(each -> InventoryManipulator.add(guest.getCharacter(), each));
        guestInventory.getItems()
                .forEach(each -> InventoryManipulator.add(creator.getCharacter(), each));
        creatorInventory.clear();
        guestInventory.clear();
        guestMoney.addAndGet(-GameConstants.calculateTradeTax(guestMoney.get()));
        creatorMoney.addAndGet(-GameConstants.calculateTradeTax(creatorMoney.get()));
        creator.getCharacter().addMoney(guestMoney.getAndSet(0), false);
        guest.getCharacter().addMoney(creatorMoney.getAndSet(0), false);
    }

    private Inventory getInventoryByVisitor(MiniRoomVisitor visitor) {
        if (visitor.getCharIndex() == 0)
            return creatorInventory;
        else if (visitor.getCharIndex() == 1)
            return guestInventory;
        return null;
    }

    private AtomicBoolean getConfirmedByVisitor(MiniRoomVisitor visitor) {
        if (visitor.getCharIndex() == 0)
            return creatorConfirmed;
        else if (visitor.getCharIndex() == 1)
            return guestConfirmed;
        return null;
    }

    private AtomicLong getMoneyByVisitor(MiniRoomVisitor visitor) {
        if (visitor.getCharIndex() == 0)
            return creatorMoney;
        else if (visitor.getCharIndex() == 1)
            return guestMoney;
        return null;
    }


    private boolean checkSpace(MiniRoomVisitor visitor, List<Item> items) {
        AtomicBoolean result = new AtomicBoolean(false);
        // TODO
        return true;
    }
}
