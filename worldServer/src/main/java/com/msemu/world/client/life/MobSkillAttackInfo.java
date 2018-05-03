package com.msemu.world.client.life;

import com.msemu.commons.utils.types.Position;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/5/2.
 */
@Getter
@Setter
public class MobSkillAttackInfo {
    private byte actionAndDirMask;
    private byte actionAndDir;
    private int targetInfo;
    private short skillID;
    private List<Position> multiTargetForBalls = new ArrayList<>();
    private List<Short> randTimeForAreaAttacks = new ArrayList<>();
    private Position oldPos;
    private Position oldVPos;
    private int encodedGatherDuration;
}
