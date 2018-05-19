package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Pet;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_PetSkillChanged extends OutPacket<GameClient> {
    public LP_PetSkillChanged(Pet pet, boolean flagAdded, int flag) {
        super(OutHeader.LP_UserPetSkillChanged);
        encodeLong(pet.getId());
        encodeByte(flagAdded);
        encodeShort(flag);
    }
}
