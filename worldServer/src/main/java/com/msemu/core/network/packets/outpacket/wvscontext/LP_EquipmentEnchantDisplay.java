package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

public class LP_EquipmentEnchantDisplay extends OutPacket<GameClient> {

    public LP_EquipmentEnchantDisplay() {
        super(OutHeader.LP_EquipmentEnchantDisplay);



    }
}
