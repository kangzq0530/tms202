package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.remote.LP_UserSetActivePortableChair;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.Inventory;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.constants.ItemConstants;

/**
 * Created by Weber on 2018/5/15.
 */
public class CP_UserPortableChairSitRequest extends InPacket<GameClient> {

    private int itemID;
    private int position;
    private boolean inBag;
    private boolean isTextChair;
    private String text;

    public CP_UserPortableChairSitRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        skip(4);
        itemID = decodeInt();
        position = decodeInt();
        inBag = decodeByte() > 0;
        isTextChair = decodeInt() > 0;
        if(isTextChair) {
            text = decodeString();
        }
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        Inventory inv = chr.getInventoryByType(ItemConstants.getInvTypeFromItemID(itemID));
        Item item = inv.getItemBySlot((short) position);
        if(item != null) {
            // use chair recalulate heal
            chr.getField().broadcastPacket(new LP_UserSetActivePortableChair(chr, item, text));
            chr.enableActions();

        }
    }
}
