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

package com.msemu.world.client.character.jobs;

import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.jobs.adventurer.Archer;
import com.msemu.world.client.character.jobs.adventurer.Beginner;
import com.msemu.world.client.character.jobs.adventurer.Magician;
import com.msemu.world.client.character.jobs.adventurer.Warrior;
import com.msemu.world.client.character.jobs.legend.Aran;
import com.msemu.world.client.character.jobs.legend.Evan;
import com.msemu.world.client.character.jobs.sengoku.Kanna;
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
            Magician.class,
            Aran.class,
            Evan.class,
            Kanna.class
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
