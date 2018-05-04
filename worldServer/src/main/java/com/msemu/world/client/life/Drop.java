package com.msemu.world.client.life;

import com.msemu.commons.utils.types.FileTime;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.enums.DropEnterType;
import com.msemu.world.enums.DropMotionType;
import com.msemu.world.enums.DropType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/11.
 */
@Getter
@Setter
public class Drop extends Life {

    private Item item;
    private int money;
    private DropType dropType;
    private DropEnterType dropEnterType = DropEnterType.Create;
    private DropMotionType dropMotionType =DropMotionType.Normal;
    private int ownerID;
    private boolean explosiveDrop;
    private FileTime expireTime;
    private boolean byPet;
    private int ownType;


    public Drop(int objectId) {
        super(objectId);
    }

    public Drop(int objectID, Item item) {
        super(objectID);
        this.item = item;
        dropType = DropType.ITEM;
        expireTime = FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME);
    }

    public Drop(int objectId, int money) {
        super(objectId);
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
}
