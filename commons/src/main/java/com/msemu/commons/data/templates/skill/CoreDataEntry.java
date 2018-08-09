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

package com.msemu.commons.data.templates.skill;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/5/7.
 */
@Getter
public class CoreDataEntry {

    public List<Integer> job;
    public List<Integer> connectSkill;
    private int coreId;
    private String name;
    private String desc;
    private int type;
    private int maxLevel;

    public CoreDataEntry(int coreId, String name, String desc, int type, int maxLevel, List<Integer> job, List<Integer> connectSkill) {
        this.coreId = coreId;
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.maxLevel = maxLevel;
        this.job = new ArrayList<>(job);
        this.connectSkill = new ArrayList<>(connectSkill);
    }

    @Override
    public String toString() {
        return "[V技能核心] 核心編號：" + this.coreId + " 名稱：" + this.name + " 描述：" + this.desc;
    }

}
