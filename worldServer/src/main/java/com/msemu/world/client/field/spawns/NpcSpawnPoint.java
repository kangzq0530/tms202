package com.msemu.world.client.field.spawns;

import com.msemu.commons.data.templates.field.LifeData;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.Npc;
import com.msemu.world.data.NpcData;

import java.time.LocalDateTime;

/**
 * Created by Weber on 2018/5/14.
 */
public class NpcSpawnPoint extends AbstractSpawnPoint {

    private boolean noFoothold;

    public NpcSpawnPoint(LifeData lifeData) {
        super(lifeData);
        this.noFoothold = lifeData.isNofoothold();
    }

    @Override
    public boolean shouldSpawn(LocalDateTime time) {
        return getSpawnedCount().get() == 0;
    }

    @Override
    public Npc spawn(Field field) {
        if (getSpawnedCount().compareAndSet(0, 1)) {
            Npc npc = NpcData.getInstance().getNpcFromTemplate(getTemplateId());
            npc.setFh(getFh());
            npc.setF(getF());
            npc.setRx0(getRx0());
            npc.setRx1(getRx1());
            npc.setUseNight(isUseNight());
            npc.setUseDay(isUseDay());
            npc.setCy(getCy());
            npc.setPosition(getPosition());
            npc.setLimitedName(getLimitedName());
            field.spawnLife(npc);
            return npc;
        }
        return null;
    }
}
