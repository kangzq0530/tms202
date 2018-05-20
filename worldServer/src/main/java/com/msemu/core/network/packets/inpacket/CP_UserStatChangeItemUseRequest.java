package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.enums.FieldLimit;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.field.Field;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Weber on 2018/5/21.
 */
public class CP_UserStatChangeItemUseRequest extends InPacket<GameClient> {

    private int updateTick;
    private int slot;
    private int itemID;

    public CP_UserStatChangeItemUseRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        this.updateTick = decodeInt();
        this.slot = decodeShort();
        this.itemID = decodeInt();
    }

    @Override
    public void runImpl() {

        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();

        final Item usedItem = chr.getConsumeInventory()
                .getItemBySlot(slot);

        final int usedCoolTime = field.getConsumeItemCoolTime();

        final boolean useLimited = field.checkFieldLimit(FieldLimit.ConsumeStatChangeItem);

        if(usedItem == null || usedItem.getItemId() != itemID || usedItem.getQuantity() < 1 || useLimited) {
            chr.enableActions();
            return;
        }

        if(chr.getLastUseStatChangeItemTime()
                .plus(usedCoolTime, ChronoUnit.SECONDS)
                .isBefore(LocalDateTime.now())) {
            //TODO dropMessage 目前無法使用。
            chr.enableActions();
            return;
        }

        //TODO apply Item


    }
}
