package com.msemu.world.client.character;

import com.msemu.commons.utils.types.Position;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/19.
 */
@Getter
@Setter
public class SkillUseInfo {

    private int updateTick;
    private int skillID;
    private byte slv;
    private boolean zeroSkill;
    private int option;
    private Position position;
    private Position position2;
    private int bulletConsumeItemID;
    private boolean affectedMemberBitmap;

}
