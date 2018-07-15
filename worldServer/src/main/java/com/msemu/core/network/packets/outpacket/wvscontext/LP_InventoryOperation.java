package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.data.enums.InvType;
import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.inventory.InventoryOperationInfo;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.enums.InventoryOperationType;

import java.util.Collections;
import java.util.List;

import static com.msemu.commons.data.enums.InvType.*;

/**
 * Created by Weber on 2018/4/13.
 */
public class LP_InventoryOperation extends OutPacket<GameClient> {

    public LP_InventoryOperation(boolean exclRequestSent, boolean notRemoveAddInfo, InventoryOperationType type,
                                 Item item, int oldBagIndex, short oldQuantity) {
        this(exclRequestSent, notRemoveAddInfo, Collections.singletonList(new InventoryOperationInfo(type, item, oldBagIndex, oldQuantity)));
    }


    public LP_InventoryOperation(boolean exclRequestSent, boolean notRemoveAddInfo, InventoryOperationType type,
                                 Item item, int oldBagIndex) {
        this(exclRequestSent, notRemoveAddInfo, Collections.singletonList(new InventoryOperationInfo(type, item, oldBagIndex)));
    }

    public LP_InventoryOperation(boolean exclRequestSent, boolean notRemoveAddInfo, List<InventoryOperationInfo> operates) {
        super(OutHeader.LP_InventoryOperation);
        encodeByte(exclRequestSent);
        encodeByte(operates.size());
        encodeByte(notRemoveAddInfo);
        operates.forEach(op -> {
            Item item = op.getItem();
            InvType invType = item.getInvType();
            if ( invType == EQUIPPED ) {
                invType = EQUIP;
            }
            encodeByte(op.getType().getValue());
            encodeByte(invType.getValue());
            encodeShort(op.getOldBagIndex());
            switch (op.getType()) {
                case ADD:
                    op.getItem().encode(this);
                    break;
                case REMOVE:
                    if (invType == EQUIP && (op.getOldBagIndex() < 0)) {
                        encodeByte(false);
                    }
                    break;
                case UPDATE:
                    encodeShort(item.getQuantity());
                    break;
                case MOVE:
                    int bagIndex = item.getInvType() == EQUIPPED ? -item.getBagIndex() : item.getBagIndex();
                    encodeShort(bagIndex);
                    if (invType == EQUIP && (op.getOldBagIndex() < 0 || bagIndex < 0)) {
                        encodeByte(false);
                    }
                    break;
                case ITEM_EXP:
                    encodeInt(((Equip) item).getExp());
                    break;
                case MOVE_TO_BAG:
                    encodeInt(item.getBagIndex());
                    break;
                case UPDATE_IN_BAG:
                    int nCurItemID = item.getBagIndex();
                    int v109;
                    if (invType == CONSUME || invType == INSTALL) {
                        v109 = 212;
                    } else if (invType == ETC) {
                        v109 = 712;
                    } else {
                        v109 = 100;
                    }
                    if (invType.getValue() > 2 // nBagMaxSlot
                            || nCurItemID < 101
                            || nCurItemID > v109) {
                        break;
                    }
                    encodeShort(item.getBagIndex());
                    break;
                case REMOVE_IN_BAG:
                    break;
                case MOVE_IN_BAG:
                    encodeShort(item.getBagIndex());
                    break;
                case ADD_IN_BAG:
                    item.encode(this);
                    break;

            }
            encodeByte(false);
        });

//        if (nBeforeCount[0]) {
//            encodeByte(false);
//        }
    }
}
