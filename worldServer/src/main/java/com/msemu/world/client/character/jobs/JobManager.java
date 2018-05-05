package com.msemu.world.client.character.jobs;

import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.jobs.adventurer.Archer;
import com.msemu.world.client.character.jobs.adventurer.Beginner;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Weber on 2018/4/23.
 */
public class JobManager {
    private static final Class<?>[] jobClasses = new Class<?>[]{
            Archer.class,
            Beginner.class,
    };

    public static JobHandler getJobHandler(short id, Character chr) {
        JobHandler job = null;
        for(Class clazz : jobClasses) {
            try {
                job = (JobHandler) clazz.getConstructor(Character.class).newInstance(chr);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            if(job != null && job.isHandlerOfJob(id)) {
                return job;
            }
        }
        return job;
    }
}
