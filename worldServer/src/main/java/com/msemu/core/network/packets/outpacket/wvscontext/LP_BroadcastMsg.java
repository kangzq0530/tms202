package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.inventory.items.Item;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_BroadcastMsg extends OutPacket<GameClient> {
    public LP_BroadcastMsg(int type, int value, String[] messages,
                           boolean bool, final Item item) {
        super(OutHeader.LP_BroadcastMsg);
        encodeByte(type);

        switch (type) {

        }

    }
}
