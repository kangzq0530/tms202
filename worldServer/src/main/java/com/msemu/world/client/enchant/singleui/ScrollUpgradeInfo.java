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

package com.msemu.world.client.enchant.singleui;

import com.msemu.commons.data.enums.EnchantStat;
import com.msemu.world.enums.ScrollIcon;
import com.msemu.world.enums.UpgradeScrollType;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;
import java.util.Map;

@Getter
@Setter
public class ScrollUpgradeInfo {

    private ScrollIcon icon;
    private String title = "";
    private UpgradeScrollType type;
    private int option;
    private Map<EnchantStat, Integer> stats = new EnumMap<>(EnchantStat.class);
    private int cost;
    private int prop;
    private int costBefore;

    public void addStat(EnchantStat stat, int value) {
        getStats().put(stat, value);
    }

    public int getStatsMask() {
        int mask = 0;
        for(EnchantStat stat : stats.keySet()) {
            mask |= stat.getValue();
        }
        return mask;
    }
}
