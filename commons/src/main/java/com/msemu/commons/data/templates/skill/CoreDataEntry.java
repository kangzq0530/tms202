package com.msemu.commons.data.templates.skill;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/5/7.
 */
@Getter
public class CoreDataEntry {

    private int coreId;
    private String name;
    private String desc;
    private int type;
    private int maxLevel;
    public List<Integer> job;
    public List<Integer> connectSkill;

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
