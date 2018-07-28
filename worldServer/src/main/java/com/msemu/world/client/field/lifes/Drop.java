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

package com.msemu.world.client.field.lifes;

import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.drops.LP_DropEnterField;
import com.msemu.core.network.packets.outpacket.drops.LP_DropLeaveField;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.field.AbstractFieldObject;
import com.msemu.world.enums.*;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Weber on 2018/4/11.
 */
@Getter
@Setter
public class Drop extends AbstractFieldObject {

    private Item item;
    private int money;
    private DropType dropType;
    private DropMotionType dropMotionType = DropMotionType.Normal;
    private int ownerID;
    private boolean explosiveDrop;
    private FileTime expireTime;
    private boolean byPet;
    private int ownType;
    private boolean picked;
    private int pickupId;
    private Lock lock = new ReentrantLock();


    public Drop(int objectId) {
        setObjectId(objectId);
    }

    public Drop(int objectId, Item item) {
        setObjectId(objectId);
        this.item = item;
        dropType = DropType.ITEM;
        expireTime = FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME);
    }

    public Drop(int objectId, int money) {
        setObjectId(objectId);
        this.money = money;
        dropType = DropType.MONEY;
        expireTime = FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME);
    }

    public void setItem(Item item) {
        this.item = item;
        setDropType(DropType.ITEM);
    }

    public boolean isMoney() {
        return getDropType() == DropType.MONEY;
    }

    public void setMoney(int money) {
        this.money = money;
        setDropType(DropType.MONEY);
    }

    public byte getItemGrade() {
        byte res = 0;
        if (getItem() != null && getItem() instanceof Equip) {
            res = (byte) ((Equip) getItem()).getGrade();
        }
        return res;
    }

    @Override
    public FieldObjectType getFieldObjectType() {
        return FieldObjectType.DROP;
    }

    @Override
    public void enterScreen(GameClient client) {
        client.write(new LP_DropEnterField(DropEnterType.OnTheFoothold, this, 100, 100, getPosition(), 0, getPosition(), 0, true, (short) 0, false, false));
    }

    @Override
    public void outScreen(GameClient client) {
        client.write(new LP_DropLeaveField(DropLeaveType.ByScreenScroll, 0, getObjectId(), (short) 0, 0, 0));
    }

    public boolean isExpired() {
        return getExpireTime() != null && getExpireTime().after(FileTime.now());
    }

}
