package com.msemu.commons.data.templates.skill;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/23.
 */
public class CarnivalInfo {
    @Getter
    @Setter
    private List<CarnivalSkillInfo> skills = new ArrayList<>();
    @Getter
    @Setter
    private List<CarnivalGuardianInfo> guardians = new ArrayList<>();
}
