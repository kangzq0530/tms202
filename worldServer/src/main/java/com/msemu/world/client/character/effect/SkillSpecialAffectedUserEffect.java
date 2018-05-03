package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class SkillSpecialAffectedUserEffect implements IUserEffect {

    private int skillID;
    private byte slv;

    public SkillSpecialAffectedUserEffect(int skillID, byte slv) {
        this.skillID = skillID;
        this.slv = slv;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.SkillSpecialAffected;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(skillID);
        outPacket.encodeByte(slv);
    }
}
