package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.remote.LP_UserEmotion;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.Inventory;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.field.Field;

/**
 * Created by Weber on 2018/5/22.
 */
public class CP_UserEmotion extends InPacket<GameClient> {

    private int emtion;
    private int duration;
    private boolean byItemOption;

    public CP_UserEmotion(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        this.emtion = decodeInt();
        this.duration = decodeInt();
        this.byItemOption = decodeByte() > 0;
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final Inventory cashInv = chr.getCashInventory();
        final Field field = chr.getField();
        if(emtion > 7) {
            final int cashEmotionId = 5160000 + (emtion - 7);
            final Item emotionItem = cashInv.getItemByItemID(cashEmotionId);
            if(emotionItem == null) {
                chr.enableActions();
                return;
            }
        }
        field.broadcastPacket(new LP_UserEmotion(chr, emtion, duration,byItemOption), chr);
    }
}
