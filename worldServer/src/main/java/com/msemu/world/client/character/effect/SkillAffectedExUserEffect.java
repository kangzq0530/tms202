package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class SkillAffectedExUserEffect implements IUserEffect {
    private int skillID;
    private byte slv;
    private int select;
    private int rootSelect;

    public SkillAffectedExUserEffect(int skillID, byte slv, int select, int rootSelect) {
        this.skillID = skillID;
        this.slv = slv;
        this.select = select;
        this.rootSelect = rootSelect;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.SkillAffectedEx;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(skillID);
        outPacket.encodeByte(slv);
        outPacket.encodeInt(select);
        outPacket.encodeInt(rootSelect);
    }
}
