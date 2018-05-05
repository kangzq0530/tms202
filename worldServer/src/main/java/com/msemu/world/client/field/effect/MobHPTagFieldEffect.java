package com.msemu.world.client.field.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.life.Mob;
import com.msemu.world.enums.FieldEffectType;

/**
 * Created by Weber on 2018/4/13.
 */
public class MobHPTagFieldEffect implements IFieldEffect {

    private final Mob mob;

    public MobHPTagFieldEffect(Mob mob) {
        this.mob = mob;
    }

    @Override
    public FieldEffectType getType() {
        return FieldEffectType.MobHPTag;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(mob.getTemplateId());
        int maxHP = (int) Math.min(Integer.MAX_VALUE, mob.getMaxHp());
        double ratio = mob.getMaxHp() / (double) Integer.MAX_VALUE;
        outPacket.encodeLong(mob.getHp());
        outPacket.encodeLong(mob.getMaxHp());
        outPacket.encodeByte(mob.getHpTagColor());
        outPacket.encodeByte(mob.getHpTagBgcolor());
    }
}
