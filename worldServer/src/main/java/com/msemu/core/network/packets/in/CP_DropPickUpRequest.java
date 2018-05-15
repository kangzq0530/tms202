package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.Drop;

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
        try  {
            if(drop.isPicked())
                return;
            if(drop.isMoney()) {
                chr.addMoney(drop.getMoney(), false);
            } else {
                chr.addItemToInventory(drop.getItem());
            }
            drop.setPicked(true);
            drop.setPickupId(chr.getId());
            field.removeDropFromUser(drop);
        } finally {
            drop.getLock().unlock();
        }


    }
}
