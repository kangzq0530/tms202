package com.msemu.world.data;

import com.msemu.commons.data.loader.wz.SkillInfoLoader;
import com.msemu.commons.data.templates.skill.MobSkillInfo;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.commons.wz.WzManager;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.skills.Skill;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        WzManager wzManager = WorldWzManager.getInstance();
        this.skillsInfo.putAll(new SkillInfoLoader().load(wzManager));
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
        return this.skillsInfo.get(skillID);
    }

    public Skill getSkillById(int skillID) {
        SkillInfo si = getSkillInfoById(skillID);
        Skill skill = new Skill();
        skill.setSkillId(si.getSkillId());
        skill.setRootId(si.getRootId());
        skill.setMasterLevel(si.getMasterLevel());
        skill.setMaxLevel(si.getMaxLevel());
        if (si.getMasterLevel() <= 0) {
            skill.setMasterLevel(skill.getMaxLevel());
        }
        if (si.getFixLevel() > 0) {
            skill.setCurrentLevel(si.getFixLevel());
        } else {
            skill.setCurrentLevel(0);
        }
        return skill;
    }

    public MobSkillInfo getMobSkillInfoByIdAndLevel(short skill, short level) {
        throw new NotImplementedException();
    }

    public List<Skill> getSkillsByJob(short jobID) {
        List<Skill> res = new ArrayList<>();
        getSkillsInfo().forEach((skillId, skillsInfo) -> {
            if (skillsInfo.getRootId() == jobID && !skillsInfo.isInvisible()) {
                res.add(getSkillById(skillId));
            }
        });
        return res;
    }
}