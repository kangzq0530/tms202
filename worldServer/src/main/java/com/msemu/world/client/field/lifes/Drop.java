package com.msemu.world.client.field.lifes;

import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.out.drops.LP_DropEnterField;
import com.msemu.core.network.packets.out.drops.LP_DropLeaveField;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.field.AbstractFieldObject;
import com.msemu.world.enums.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

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

    public void setMoney(int money) {
        this.money = money;
        setDropType(DropType.MONEY);
    }

    public boolean isMoney() {
        return getDropType() == DropType.MONEY;
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
        return getExpireTime() != null && getExpireTime().after(FileTime.getTime());
    }

}
