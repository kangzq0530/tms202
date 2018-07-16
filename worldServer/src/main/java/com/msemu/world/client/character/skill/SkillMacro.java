package com.msemu.world.client.character.skill;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
public class SkillMacro implements Serializable {

    private int macroId;
    private int skill1;
    private int skill2;
    private int skill3;
    private String name = "";
    private int shout;
    private int position;
}
