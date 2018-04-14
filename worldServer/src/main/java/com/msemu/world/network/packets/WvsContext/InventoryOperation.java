package com.msemu.world.network.packets.WvsContext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.character.items.Equip;
import com.msemu.world.client.character.items.Item;
import com.msemu.world.enums.InvType;
import com.msemu.world.enums.InventoryOperationType;

/**
 * Created by Weber on 2018/4/13.
 */
public class InventoryOperation extends OutPacket {


    public InventoryOperation(boolean exclRequestSent, boolean notRemoveAddInfo, InventoryOperationType type, short oldPos, short newPos, int bagPos, Item item) {
        super(OutHeader.InventoryOperation);

        InvType invType = item.getInvType();
        if (oldPos > 0 && newPos < 0 && invType == InvType.EQUIPPED) {
            invType = InvType.EQUIP;
        }

        encodeByte(exclRequestSent);
        encodeByte(1); // size
        encodeByte(notRemoveAddInfo);

        encodeByte(type.getValue());
        encodeByte(invType.getValue());
        encodeShort(oldPos);
        switch (type) {
            case ADD:
                item.encode(this);
                break;
            case UPDATE_QUANTITY:
                encodeShort(item.getQuantity());
                break;
            case MOVE:
                encodeShort(newPos);
                if (invType == InvType.EQUIP && (oldPos < 0 || newPos < 0)) {
                    encodeByte(item.getCashItemSerialNumber() > 0);
                }
                break;
            case REMOVE:
                break;
            case ITEM_EXP:
                encodeLong(((Equip) item).getExp());
                break;
            case UPDATE_BAG_POS:
                encodeInt(bagPos);
                break;
            case UPDATE_BAG_QUANTITY:
                encodeShort(newPos);
                break;
            case UNK_1:
                break;
            case UNK_2:
                encodeShort(bagPos); // ?
                break;
            case UPDATE_ITEM_INFO:
                item.encode(this);
                break;
            case UNK_3:
                break;
        }
    }
}
