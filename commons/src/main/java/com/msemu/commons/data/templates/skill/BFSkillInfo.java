package com.msemu.commons.data.templates.skill;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/23.
 */
@Getter
@Setter
public class BFSkillInfo {
    private String name;
    private String desc;
    private int mobSkillID;
    private boolean userCount;
    private int owner;
    private String effect;
    private int level;
    boolean allMap;
}
