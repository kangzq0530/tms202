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

package com.msemu.world.client.field.spawns;

import com.msemu.commons.data.templates.field.LifeInField;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.ForcedMobStat;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.data.MobData;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Weber on 2018/5/13.
 */
public class MobSpawnPoint extends AbstractSpawnPoint {

    @Getter
    @Setter
    private int mobTime, regenStart, mobAliveReq;

    @Getter
    @Setter
    private boolean mobTimeOnDie, dummy, spine;

    @Getter
    @Setter
    private String limitedName;

    @Getter
    @Setter
    private LocalDateTime nextSpawnTime;

    @Getter
    @Setter
    private ForcedMobStat forcedMobStat;


    public MobSpawnPoint(LifeInField lifeInField) {
        super(lifeInField);
        this.mobTime = lifeInField.getMobTime();
        this.regenStart = lifeInField.getRegenStart();
        this.mobAliveReq = lifeInField.getMobAliveReq();
        this.mobTimeOnDie = lifeInField.isMobTimeOnDie();
        this.dummy = lifeInField.isDummy();
        this.spine = lifeInField.isSpine();
        this.limitedName = lifeInField.getLimitedname();
        this.nextSpawnTime = LocalDateTime.now();
    }

    @Override
    public boolean shouldSpawn(LocalDateTime time) {

        if (getMobTime() < 0)
            return false;

        if ((mobTime != 0 && getSpawnedCount().get() > 0) || getSpawnedCount().get() > 1) {
            return false;
        }

        return getNextSpawnTime().isBefore(time);
    }

    @Override
    public Mob spawn(Field field) {

        if (getSpawnedCount().compareAndSet(0, 1)) {
            Mob mob = MobData.getInstance().getMobFromTemplate(getTemplateId());
            mob.setPosition(getPosition());
            mob.setF(getF());
            mob.setFh(getFh());
            mob.setOriginFh(getFh());
            mob.setMobListener(() -> {
                nextSpawnTime = LocalDateTime.now();
                if (getMobTime() > 0) {
                    nextSpawnTime = nextSpawnTime.plus(getMobTime(), ChronoUnit.MILLIS);
                }
                getSpawnedCount().getAndDecrement();
            });
            field.spawnLife(mob);
            return mob;
        }

        return null;
    }
}
