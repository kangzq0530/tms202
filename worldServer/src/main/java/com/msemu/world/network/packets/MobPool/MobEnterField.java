package com.msemu.world.network.packets.MobPool;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.life.Mob;
import com.msemu.world.client.life.ShootingMoveStat;
import com.msemu.world.client.life.skills.ForcedMobStat;

/**
 * Created by Weber on 2018/4/12.
 */
public class MobEnterField extends OutPacket {

    public MobEnterField(Mob mob, boolean hasBeenInit) {
        super(OutHeader.MobEnterField);
        encodeByte(mob.isSealedInsteadDead());
        encodeInt(mob.getObjectId());
        encodeByte(mob.getCalcDamageIndex());
        encodeInt(mob.getTemplateId());
        ForcedMobStat fms = mob.getForcedMobStat();
        encodeByte(fms != null);
        if (fms != null) {
            fms.encode(this);
        }
        mob.getTemporaryStat().encode(this);
        if (!hasBeenInit) {
            // CMob::Init
            encodePosition(mob.getPosition());
            encodeByte(mob.getMoveAction());
//           encodeByte(0); // banban boss?
            encodeShort(mob.getCurFoodhold().getId());
            encodeShort(mob.getHomeFoothold().getId());
            byte appearType = mob.getAppearType();
            encodeShort(appearType);
            if (appearType == -3 || appearType >= 0) {
                // init -> -2, -1 else
                encodeInt(mob.getOption());
            }
            encodeByte(mob.getTeamForMCarnival());
            encodeInt(mob.getHp() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) mob.getHp());
            encodeInt(mob.getEffectItemID());
            if (mob.isPatrolMob()) {
                encodeInt(mob.getPatrolScopeX1());
                encodeInt(mob.getPatrolScopeX2());
                encodeInt(mob.getDetectX());
                encodeInt(mob.getSenseX());
            }
            encodeInt(mob.getPhase());
            encodeInt(mob.getCurZoneDataType());
            encodeInt(mob.getRefImgMobID());
            encodeInt(0); // ?
            int ownerAID = mob.getLifeReleaseOwnerAID();
            encodeByte(ownerAID > 0);
            if (ownerAID > 0) {
                encodeInt(ownerAID);
                encodeString(mob.getLifeReleaseOwnerName());
                encodeString(mob.getLifeReleaseMobName());
            }
            encodeInt(mob.getAfterAttack());
            encodeInt(mob.getCurrentAction());
            encodeByte(mob.isLeft());
            int size = 0;
            encodeInt(size);
            for (int i = 0; i < size; i++) {
                encodeInt(0); // ?
                encodeInt(0); // extra time?
            }
            encodeInt(mob.getScale());
            encodeInt(mob.getEliteGrade());
            if (mob.getEliteGrade() >= 0) {
                size = 0;
                encodeInt(size);
                for (int i = 0; i < size; i++) {
                    encodeInt(0); // first skillID?
                    encodeInt(0); // second skillID?
                }
                encodeInt(mob.getEliteType()); // 1 normal, 3 elite boss probably
            }
            ShootingMoveStat sms = mob.getShootingMoveStat();
            encodeByte(sms != null);
            if (sms != null) {
                sms.encode(this);
            }
            size = 0;
            encodeInt(size);
            for (int i = 0; i < size; i++) {
                encodeInt(0); // nType
                encodeInt(0); // key?
            }
            encodeInt(mob.getTargetUserIdFromServer());
            encodeInt(0);
        }
    }
}
