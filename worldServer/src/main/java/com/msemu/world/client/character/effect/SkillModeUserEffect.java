package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class SkillModeUserEffect implements IUserEffect {

    private int skillID;
    private int skillMode;
    private int skillModeStatus;

    public SkillModeUserEffect(int skillID, int skillMode, int skillModeStatus) {
        this.skillID = skillID;
        this.skillMode = skillMode;
        this.skillModeStatus = skillModeStatus;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.SkillMode;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(skillID);
        outPacket.encodeInt(skillMode);
        outPacket.encodeInt(skillModeStatus);
    }
}
