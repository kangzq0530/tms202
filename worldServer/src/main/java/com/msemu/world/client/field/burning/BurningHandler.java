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

package com.msemu.world.client.field.burning;

import com.msemu.commons.data.templates.MobTemplate;
import com.msemu.commons.utils.types.Tuple;
import com.msemu.world.client.character.ExpIncreaseInfo;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.spawns.MobSpawnPoint;
import com.msemu.world.constants.FieldConstants;
import com.msemu.world.data.MobData;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class BurningHandler {

    @Getter
    private final Field field;

    public BurningHandler(Field field) {
        this.field = field;
    }

    public void update() {
        boolean canApply = canApplyBurningField();
        if (!canApply)
            return;
        // TODO 燃燒場地邏輯
    }


    public boolean canApplyBurningField() {
        final Tuple<Integer, Integer> lv = getFieldMobLevelRange();
        return !getField().isTown() && !FieldConstants.isTutorialMap(getField().getFieldId()) && lv.getRight() >= 100;
    }

    public Tuple<Integer, Integer> getFieldMobLevelRange() {
        List<MobSpawnPoint> spawns = getField().getSpawnPoints().stream()
                .filter(sp -> sp instanceof MobSpawnPoint)
                .map(sp -> (MobSpawnPoint) sp).collect(Collectors.toList());
        int max = 0;
        int min = Short.MAX_VALUE - 1;
        for (MobSpawnPoint sp : spawns) {
            MobTemplate template = MobData.getInstance().getMobTemplate(sp.getTemplateId());
            if (min < template.getLevel()) {
                min = template.getLevel();
            }
            if (max > template.getLevel()) {
                max = template.getLevel();
            }
        }
        return new Tuple<>(min, max);
    }

    public double getBurningExpRate() {
        return 1.0;
    }

    public int getBurningStep() {
        return 0;
    }
}
