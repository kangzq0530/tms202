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

package com.msemu.world.data;

import com.msemu.commons.data.loader.dat.MobTemplateDatLoader;
import com.msemu.commons.data.loader.wz.WzManager;
import com.msemu.commons.data.templates.MobTemplate;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.quest.req.QuestProgressMobRequirement;
import com.msemu.world.client.field.lifes.Mob;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/11.
 */
@Reloadable(name = "mob", group = "all")
@StartupComponent("Data")
public class MobData implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(MobData.class);
    private static final AtomicReference<MobData> instance = new AtomicReference<>();
    @Getter
    private final MobTemplateDatLoader mobTemplateDatLoader = new MobTemplateDatLoader();


    public MobData() {
        load();
    }

    public static MobData getInstance() {
        MobData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new MobData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public void load() {
        WzManager wzManager = WorldWzManager.getInstance();
        getMobTemplateDatLoader().load();
        linkMobAndQuest();
        log.info("{} MobTemplates laoded", getMobTemplateDatLoader().getData().size());
    }

    public void clear() {
        getMobTemplateDatLoader().getData().clear();
    }

    @Override
    public void reload() {
        clear();
        load();
    }

    public Mob getMobFromTemplate(int templateId) {
        DropData dropData = DropData.getInstance();
        MobTemplate mt = getMobTemplateDatLoader().getItem(templateId);
        if (mt == null)
            return null;
        Mob mob = new Mob(-1, mt);
        mob.setDropsInfo(dropData.getDropsInfoByMobID(templateId));
        mob.init();
        return mob;
    }

    public MobTemplate getMobTemplate(int templateId) {
        return getMobTemplateDatLoader().getItem(templateId);
    }

    public void linkMobAndQuest() {
        QuestData.getInstance().getQuestsProgressRequirements()
                .forEach((questID, reqs) -> {
                    reqs.stream().filter(req -> req instanceof QuestProgressMobRequirement)
                            .map(req -> (QuestProgressMobRequirement) req)
                            .forEach(req -> {
                                MobTemplate mt = getMobTemplate(req.getMobID());
                                if (mt != null) {
                                    mt.addLinkedQuest(questID);
                                }
                            });
                });
    }
}