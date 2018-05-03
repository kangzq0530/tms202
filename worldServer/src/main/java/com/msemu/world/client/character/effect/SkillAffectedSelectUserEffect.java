package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class SkillAffectedSelectUserEffect implements IUserEffect {
    private int select;
    private int rootSelect;
    private int skillID;
    private byte slv;
    private boolean special;

    public SkillAffectedSelectUserEffect(int select, int rootSelect, int skillID, byte slv, boolean special) {
        this.select = select;
        this.rootSelect = rootSelect;
        this.skillID = skillID;
        this.slv = slv;
        this.special = special;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.SkillAffectedSelect;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(select);
        outPacket.encodeInt(rootSelect);
        outPacket.encodeInt(skillID);
        outPacket.encodeByte(slv);
        outPacket.encodeByte(special);
    }
}
