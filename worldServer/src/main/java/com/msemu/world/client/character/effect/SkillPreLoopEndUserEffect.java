package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class SkillPreLoopEndUserEffect implements IUserEffect {

    private int skillID;
    private int duration;

    public SkillPreLoopEndUserEffect(int skillID, int duration) {
        this.skillID = skillID;
        this.duration = duration;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.SkillPreLoopEnd;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(skillID);
        outPacket.encodeInt(duration);
    }
}
