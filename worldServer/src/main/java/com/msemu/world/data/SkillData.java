package com.msemu.world.data;

import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.commons.utils.DirUtils;
import com.msemu.commons.utils.StringUtils;
import com.msemu.commons.utils.XMLApi;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import com.msemu.core.configs.CoreConfig;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.skills.Skill;
import com.msemu.world.client.character.skills.SkillInfo;
import com.msemu.world.client.life.skills.MobSkillInfo;
import com.msemu.world.data.annotations.DataLoader;
import com.msemu.world.data.annotations.DataSaver;
import com.msemu.world.enums.MobSkillStat;
import com.msemu.world.enums.SkillStat;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/11.
 */
@Reloadable(name = "skill", group = "all")
@StartupComponent("Data")
public class SkillData implements IReloadable {

    private static final Logger log = LoggerFactory.getLogger(QuestData.class);

    @Getter
    private final Map<Integer, SkillInfo> skillsInfo = new HashMap<>();

    @Getter
    private final Map<Integer, MobSkillInfo> mobSkillsInfo = new HashMap<>();

    private static final AtomicReference<SkillData> instance = new AtomicReference<>();


    public static SkillData getInstance() {
        SkillData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new SkillData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public SkillData() {
        load();
    }

    public void load() {

        log.info("{} SkillInfo loaded", this.skillsInfo.size());
    }

    public void clear() {
        this.skillsInfo.clear();
    }

    @Override
    public void reload() {
        clear();
        load();
    }

    public SkillInfo getSkillInfoById(int skillID) {
        throw new NotImplementedException();
    }

    public Skill getSkillById(int id) {
        throw new NotImplementedException();
    }

    public MobSkillInfo getMobSkillInfoByIdAndLevel(short skill, short level) {
        throw new NotImplementedException();
    }
}