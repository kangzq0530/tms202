package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_Message;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.messages.DropPickUpAddIenventoryItem;
import com.msemu.world.client.character.messages.DropPickUpItemFullFail;
import com.msemu.world.client.character.messages.DropPickUpOwnerShipFail;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.Drop;
import com.msemu.world.constants.GameConstants;

/**
 * Created by Weber on 2018/5/16.
 */
public class CP_DropPickUpRequest extends InPacket<GameClient> {

    private byte fieldKey;
    private int updateTick;
    private Position position;
    private int objectID;
    private int itemCrc;
    private boolean forgeWindow;
    private int forgeWindowHWND;
    private int gameWindowHWND;

    public CP_DropPickUpRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        fieldKey = decodeByte();
        updateTick = decodeInt();
        position = decodePosition();
        objectID = decodeInt();
        itemCrc = decodeInt();
        forgeWindow = decodeByte() > 0;
        forgeWindowHWND = decodeInt();
        gameWindowHWND = decodeInt();
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        Field field = chr.getField();

        Drop drop = field.getDropByObjectId(objectID);

        drop.getLock().lock();
        try {
            if (drop.isPicked()) {
                //
            } else if (drop.getOwnerID() != chr.getId()) {
                chr.write(new LP_Message(new DropPickUpOwnerShipFail()));
            } else if (drop.isMoney()) {
                if (chr.getMoney() + drop.getMoney() > GameConstants.MAX_MONEY) {
                    chr.write(new LP_Message(new DropPickUpItemFullFail()));
                } else {
                    chr.addMoney(drop.getMoney(), false);
                    drop.setPicked(true);
                    drop.setPickupId(chr.getId());
                    field.removeDropFromUser(drop);
                }
            } else if (!drop.isMoney()) {
                Item item = drop.getItem();
                if(!chr.canHold(item.getItemId(), item.getQuantity())) {
                    chr.write(new LP_Message(new DropPickUpItemFullFail()));
                } else {
                    chr.write(new LP_Message(new DropPickUpAddIenventoryItem(item)));
                    chr.addItemToInventory(item);
                    drop.setPicked(true);
                    drop.setPickupId(chr.getId());
                    field.removeDropFromUser(drop);
                }
            }
        } finally {
            drop.getLock().unlock();
        }


    }
}
