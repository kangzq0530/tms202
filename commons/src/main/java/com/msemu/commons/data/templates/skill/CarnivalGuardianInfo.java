package com.msemu.commons.data.templates.skill;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/23.
 */
public class CarnivalGuardianInfo {
    @Getter
    @Setter
    private int type;
    @Getter
    @Setter
    private int spendCP;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String desc;
    @Getter
    @Setter
    private int mobSkillID;
    @Getter
    @Setter
    private int level;

    @Override
    public String toString() {
        return String.format("[怪物擂台守護] %s - %s (%d) 消耗CP: %d 等級: %d", name, desc, mobSkillID, spendCP, level);
    }
}
