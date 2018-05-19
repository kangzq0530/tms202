package com.msemu.core.network.packets.outpacket.drops;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Drop;
import com.msemu.world.enums.DropEnterType;

/**
 * Created by Weber on 2018/4/12.
 */
public class LP_DropEnterField extends OutPacket<GameClient> {

    public LP_DropEnterField(Drop drop, Position dropPositionFrom, Position dropPositionTo, int charID) {
        this(DropEnterType.Create, drop, 100, 100, dropPositionTo, charID, dropPositionFrom, 0, false, (short) 0, false, false);
    }

    public LP_DropEnterField(DropEnterType enterType, Drop drop, int rand,
                             int dropSpeed, Position dropPos, int sourceID,
                             Position tempPos, int delay, boolean unkBool, short fallingVY,
                             boolean fadeInEffect, boolean prepareCollisionPickUp) {
        super(OutHeader.LP_DropEnterField);

        encodeByte(drop.getDropType().getVal());
        encodeByte(enterType.getVal());

        encodeInt(drop.getObjectId());
        encodeByte(drop.isMoney());
        encodeInt(drop.getDropMotionType().getVal());
        encodeInt(dropSpeed);
        encodeInt(rand);
        encodeInt(drop.getItem() == null ? drop.getMoney() : drop.getItem().getItemId());
        encodeInt(drop.getOwnerID());
        encodeByte(drop.getOwnType()); // 3 = high drop
        encodePosition(dropPos);
        encodeInt(sourceID);
        encodeByte(0); // nMakeType;
        encodeByte(0);
        encodeInt(0);
        if (enterType != DropEnterType.OnTheFoothold) {
            encodePosition(tempPos);
            encodeInt(delay);
        }
        encodeByte(drop.isExplosiveDrop());
        if (!drop.isMoney()) {
            FileTime expireTime = drop.getExpireTime();
            if (expireTime == null) {
                expireTime = FileTime.getFileTimeFromType(FileTime.Type.PERMANENT);
            }
            encodeFT(expireTime);
        }
        encodeByte(drop.isByPet());
        encodeByte(unkBool);
        encodeShort(fallingVY);
        encodeByte(fadeInEffect);
        // bCollisionPickUp
        int bCollisionPickUp = drop.getItem() == null ? 0 : drop.getItem().getItemId();
        switch (bCollisionPickUp) {
            case 2023484: // 連續擊殺模式
            case 2023494: // 連續擊殺模式
            case 2023495: // 連續擊殺模式
                //            case 2023496: // 獲得楓幣量2倍！
                //            case 2023497: // 移動速度兩倍！
                //            case 2023498: // Buff
                encodeInt(1);
                break;
            default:
                encodeInt(0);
        }
        encodeByte(drop.getItemGrade());
        encodeByte(prepareCollisionPickUp ? 1 : 0);

    }
}
