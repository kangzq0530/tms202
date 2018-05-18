package com.msemu.world.client.field.spawns;

import com.msemu.commons.data.templates.field.LifeData;
import com.msemu.commons.utils.types.Position;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.AbstractInternalAnimatedLife;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Weber on 2018/5/13.
 */
public abstract class AbstractSpawnPoint {

    @Getter
    private final String lifeType;
    @Getter
    private final AtomicInteger spawnedCount = new AtomicInteger(0);
    @Getter
    @Setter
    private int templateId;
    @Getter
    @Setter
    private int fh, f, cy, rx0, rx1;
    @Getter
    @Setter
    private boolean hide, useDay, useNight;
    @Getter
    @Setter
    private Position position;
    @Getter
    @Setter
    private String limitedName;

    public AbstractSpawnPoint(LifeData lifeData) {
        this.templateId = lifeData.getId();
        this.f = lifeData.getF();
        this.fh = lifeData.getFh();
        this.cy = lifeData.getCy();
        this.rx0 = lifeData.getRx0();
        this.rx1 = lifeData.getRx1();
        this.hide = lifeData.isHide();
        this.useDay = lifeData.isUseDay();
        this.useNight = lifeData.isUseNight();
        this.position = new Position(lifeData.getX(), lifeData.getY());
        this.lifeType = lifeData.getType();
        this.limitedName = lifeData.getLimitedname();
    }

    public abstract boolean shouldSpawn(LocalDateTime time);

    public abstract AbstractInternalAnimatedLife spawn(Field field);

}
