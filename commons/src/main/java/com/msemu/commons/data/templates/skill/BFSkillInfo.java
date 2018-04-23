package com.msemu.commons.data.templates.skill;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/23.
 */
public class BFSkillInfo {
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
    private boolean userCount;
    @Getter
    @Setter
    private int owner;
    @Getter
    @Setter
    private String effect;
    @Getter
    @Setter
    private int level;
    @Getter
    @Setter
    boolean allMap;

}
