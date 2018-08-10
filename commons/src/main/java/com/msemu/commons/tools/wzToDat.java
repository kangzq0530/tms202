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

package com.msemu.commons.tools;

import com.msemu.commons.config.utils.ConfigLoader;
import com.msemu.commons.data.loader.wz.QuestInfoLoader;
import com.msemu.commons.data.loader.wz.SkillInfoLoader;
import com.msemu.commons.data.loader.wz.WzManager;

import java.io.IOException;

/**
 * Created by Weber on 2018/4/28.
 */
public class wzToDat {

    public static void main(String[] args) throws IOException {
        ConfigLoader.getInstance().reload();
        WzManager wzManager = new WzManager();
//        new ForbiddenNameLoader(wzManager).saveToDat();
//        new ItemOptionLoader(wzManager).saveToDat();
//        new ItemTemplateLoader(wzManager).saveToDat();
//        new EquipTemplateLoader(wzManager).saveToDat();
//        new SetItemInfoLoader(wzManager).saveToDat();
//        new MonsterBookLoader(wzManager).saveToDat();
//        new FieldTemplateLoader(wzManager).saveToDat();
//        new NpcTemplateLoader(wzManager).saveToDat();
//
//        new SkillInfoLoader(wzManager).saveToDat();
        new QuestInfoLoader(wzManager).saveToDat();
//        new MobTemplateLoader(wzManager).saveToDat();
//        new FieldTemplateLoader(wzManager).saveToDat();
    }
}
