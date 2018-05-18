package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.JewelCraftType;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class JewelCraftUserEffect implements IUserEffect {

    private JewelCraftType type;

    private int itemID;

    public JewelCraftUserEffect(JewelCraftType type) {
        this.type = type;
    }

    public JewelCraftUserEffect(JewelCraftType type, int itemID) {
        this.type = type;
        this.itemID = itemID;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.JewelCraft;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(type.getValue());
        if (type == JewelCraftType.SYNTHESIZE_KEEP || type == JewelCraftType.SYNTHESIZE_FAIL)
            outPacket.encodeInt(itemID);
    }
}
