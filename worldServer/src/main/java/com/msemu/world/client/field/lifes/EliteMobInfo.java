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

package com.msemu.world.client.field.lifes;

import com.msemu.commons.utils.Rand;
import com.msemu.world.enums.EliteMobAttribute;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/5/14.
 */
public class EliteMobInfo {

    @Getter
    private final Map<EliteMobAttribute, Integer> eliteAttrs = new HashMap<>();
    @Getter
    private int grade;

    public EliteMobInfo() {
        this.grade = Rand.get(3);
        this.addEliteAttribute(EliteMobAttribute.getRandomAttribute(), 100);
    }

    public EliteMobInfo(int grade) {
        this.grade = grade;
        this.addEliteAttribute(EliteMobAttribute.getRandomAttribute(), 100);
    }

    public EliteMobInfo(int grade, Map<EliteMobAttribute, Integer> eliteAttrs) {
        this.grade = grade;
        this.eliteAttrs.putAll(eliteAttrs);
    }


    public final void addEliteAttribute(EliteMobAttribute att, int unk) {
        eliteAttrs.put(att, unk);
    }

    public void removeEliteAttribute(EliteMobAttribute att) {
        eliteAttrs.remove(att);
    }

}
