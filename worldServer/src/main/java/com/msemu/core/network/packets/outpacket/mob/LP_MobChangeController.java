/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.core.network.packets.outpacket.mob;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.ShootingMoveStat;
import com.msemu.world.client.field.lifes.ForcedMobStat;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.enums.MobAppearType;

/**
 * Created by Weber on 2018/4/12.
 */
public class LP_MobChangeController extends OutPacket<GameClient> {

    public LP_MobChangeController(Mob mob, boolean hasBeenInit, boolean isController) {
        super(OutHeader.LP_MobChangeController);
        encodeByte(isController ? mob.getControllerLevel() : 0);
        encodeInt(mob.getObjectId());
        if (isController) {
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
                switch (mob.getTemplateId()) {
                    case 8910000: // 混沌班班
                    case 8910100: // 斑斑
                    case 9990033: //
                        encodeByte(0);
                        break;
                }
                encodeShort(mob.getFh());
                encodeShort(mob.getOriginFh());
                MobAppearType appearType = mob.getAppearType();
                encodeShort(appearType.getValue());
                if (appearType.getValue() == -3 || appearType.getValue() >= 0) {
                    // init -> -2, -1 else
                    encodeInt(mob.getOption());
                }
                encodeByte(mob.getTeamForMCarnival());
                encodeLong(mob.getHp() > Long.MAX_VALUE ? Long.MAX_VALUE : (int) mob.getHp());
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
                int ownerAID = mob.getLifeReleaseOwnerAID();
                encodeByte(ownerAID > 0);
                if (ownerAID > 0) {
                    encodeInt(ownerAID);
                    encodeString(mob.getLifeReleaseOwnerName());
                    encodeString(mob.getLifeReleaseMobName());
                }
                encodeInt(-1); // ?
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
                if (mob.getEliteGrade() > 0) {
                    encodeInt(mob.getEliteMobInfo().getEliteAttrs().size());
                    mob.getEliteMobInfo().getEliteAttrs().forEach((key, value) -> {
                        encodeInt(key.getValue());
                        encodeInt(value);
                    });
                    encodeInt(1); // 1 normal, 3 elite boss probably
                }
                ShootingMoveStat sms = mob.getShootingMoveStat();
                encodeByte(sms != null);
                if (sms != null) {
                    sms.encode(this);
                }
                size = 0;
                encodeByte(size);
                for (int i = 0; i < size; i++) {
                    encodeInt(0);
                    encodeInt(0);
                }
                size = 0;
                encodeInt(size);
                for (int i = 0; i < size; i++) {
                    encodeInt(0); // nType
                    encodeInt(0); // key?
                }
                encodeInt(0x000000FF);
                encodeByte(0);
                if (mob.getTemplateId() / 10000 == 961) {
                    encodeString(mob.getTemplate().getName());
                }
                encodeInt(0);
                encodeInt(0);
                encodeZeroBytes(50);
            }
        }
    }
}
