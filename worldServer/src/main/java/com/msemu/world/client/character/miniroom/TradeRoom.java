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

import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.miniroom.LP_MiniRoom;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.miniroom.actions.ShopChatAction;
import com.msemu.world.enums.FieldObjectType;
import com.msemu.world.enums.MiniRoomType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TradeRoom extends MiniRoom {

    @Getter
    private AtomicInteger leftMoney = new AtomicInteger(0);
    @Getter
    private AtomicInteger rightMoney = new AtomicInteger(0);

    @Getter
    private AtomicBoolean leftConfirmed = new AtomicBoolean(false);
    @Getter
    private AtomicBoolean rightConfirmed = new AtomicBoolean(false);

    @Getter
    private List<Item> leftItems = new ArrayList<>();

    @Getter
    private List<Item> rightItems = new ArrayList<>();

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
        useLock(() -> {
            addVisitor(creator);
            setClosed(false);
        });
    }

    @Override
    public void enter(Character chr) {
        useLock(() -> {
            addVisitor(chr);
            if (getVisitors().size() == 2 && !isClosed()) {
                getVisitor(0).write(new LP_MiniRoom(new ShopChatAction(0, "系統提示 : 進行楓幣交換請注意手續費")));
                getVisitor(1).write(new LP_MiniRoom(new ShopChatAction(1, "系統提示 : 進行楓幣交換請注意手續費")));
                getVisitor(0).write(new LP_MiniRoom(new ShopChatAction(0, "系統提示 : 使用指令 '/幫助' 可以看到交易可用的指令說明.")));
                getVisitor(1).write(new LP_MiniRoom(new ShopChatAction(1, "系統提示 : 使用指令 '/幫助' 可以看到交易可用的指令說明.")));
                setClosed(false);
            } else {
                this.close();
            }
        });
    }

    @Override
    public void leave(Character chr) {
        useLock(() -> {
            final MiniRoomVisitor visitor = getVisitorByCharacter(chr);
            if (visitor != null) {
                removeVisitor(visitor);

            }
        });
    }

    @Override
    public void close() {
        useLock(() -> {
            setClosed(true);
            removeAllVisitors();
        });
    }

    public boolean canPutItem(Character chr) {
        return getVisitors().size() == 2 && !getLeftConfirmed().get() && !getRightConfirmed().get();
    }

    public void putItem(Character chr, Item item) {


    }

    private void addItem(Item item) {

    }

    public void completeTrade(Character chr) {

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
}
