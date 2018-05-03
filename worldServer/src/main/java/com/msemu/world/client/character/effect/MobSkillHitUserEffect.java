package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class MobSkillHitUserEffect implements IUserEffect {

    private int mobSkillID;
    private int mobSkillSlv;

    public MobSkillHitUserEffect(int mobSkillID, int mobSkillSlv) {
        this.mobSkillID = mobSkillID;
        this.mobSkillSlv = mobSkillSlv;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.MobSkillHit;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(mobSkillID);
        outPacket.encodeInt(mobSkillSlv);
    }
}
