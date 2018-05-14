package com.msemu.world.client.field.spawns;

import com.msemu.commons.data.templates.field.LifeData;
import com.msemu.commons.utils.DateTimeUtils;
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


    public MobSpawnPoint(LifeData lifeData) {
        super(lifeData);
        this.mobTime = lifeData.getMobTime();
        this.regenStart = lifeData.getRegenStart();
        this.mobAliveReq = lifeData.getMobAliveReq();
        this.mobTimeOnDie = lifeData.isMobTimeOnDie();
        this.dummy = lifeData.isDummy();
        this.spine = lifeData.isSpine();
        this.limitedName = lifeData.getLimitedname();
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
            field.spawnMob(mob);
            return mob;
        }

        return null;
    }
}
