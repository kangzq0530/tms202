package com.msemu.commons.data.templates.skill;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/23.
 */
public class CarnivalSkillInfo {
    @Getter
    @Setter
    private int target;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String desc;
    @Getter
    @Setter
    private int skillID;
    @Getter
    @Setter
    private int level;

    @Override
    public String toString() {
        return String.format("[怪物擂台技能] %s - $s (%d) 等級: %d", name, desc, skillID, level);
    }

}
