package com.msemu.world.client.character.jobs;

import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.jobs.adventurer.Archer;
import com.msemu.world.client.character.jobs.adventurer.Beginner;
import com.msemu.world.client.character.jobs.adventurer.Magician;
import com.msemu.world.client.character.jobs.adventurer.Warrior;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Weber on 2018/4/23.
 */
public class JobManager {

    private static final Logger log = LoggerFactory.getLogger(JobManager.class);

    @Getter(AccessLevel.PRIVATE)
    private static final Class<?>[] jobClasses = new Class<?>[]{
            Archer.class,
            Beginner.class,
            Warrior.class,
            Magician.class
    };

    @SuppressWarnings("unchecked")
    public static JobHandler getJobHandler(short jobID, Character chr) {
        JobHandler job = null;
        for (Class clazz : getJobClasses()) {
            try {
                job = (JobHandler) clazz.getConstructor(Character.class).newInstance(chr);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                log.error("getJobHandlerError", e);
            }
            if (job != null && job.isHandlerOfJob(jobID)) {
                return job;
            }
        }
        return job;
    }
}
