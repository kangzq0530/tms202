package com.msemu.core.network.packets.outpacket.mob;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.client.field.lifes.skills.MobSkillAttackInfo;
import com.msemu.world.client.field.lifes.movement.IMovement;

import java.util.List;

/**
 * Created by Weber on 2018/5/2.
 */
public class LP_MobMove extends OutPacket<GameClient> {

    public LP_MobMove(Mob mob, MobSkillAttackInfo msai, List<IMovement> movements) {
        super(OutHeader.LP_MobMove);
        encodeInt(mob.getObjectId());
        encodeByte(msai.getActionAndDirMask());
        encodeByte(msai.getActionAndDir());
        encodeInt(msai.getTargetInfo());
        encodeByte(msai.getMultiTargetForBalls().size());
        msai.getMultiTargetForBalls().forEach(this::encodePosition);
        encodeByte(msai.getRandTimeForAreaAttacks().size());
        msai.getRandTimeForAreaAttacks().forEach(this::encodeShort);
        encodeInt(msai.getEncodedGatherDuration());
        encodePosition(msai.getOldPos());
        encodePosition(msai.getOldVPos());
        encodeByte(movements.size());
        movements.stream().forEach(m -> m.encode(this));
        encodeByte(0);
        encodeByte(0);
    }
}
