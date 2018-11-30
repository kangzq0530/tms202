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
import com.msemu.commons.utils.types.Position;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.InternalLife;
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

    public AbstractSpawnPoint(LifeInField lifeInField) {
        this.templateId = lifeInField.getId();
        this.f = lifeInField.getF();
        this.fh = lifeInField.getFh();
        this.cy = lifeInField.getCy();
        this.rx0 = lifeInField.getRx0();
        this.rx1 = lifeInField.getRx1();
        this.hide = lifeInField.isHide();
        this.useDay = lifeInField.isUseDay();
        this.useNight = lifeInField.isUseNight();
        this.position = new Position(lifeInField.getX(), lifeInField.getY());
        this.lifeType = lifeInField.getType();
        this.limitedName = lifeInField.getLimitedname();
    }

    public abstract boolean shouldSpawn(LocalDateTime time);

    public abstract InternalLife spawn(Field field);

}
