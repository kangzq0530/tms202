package com.msemu.world.client.character;

import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/12.
 */
@Getter
@Setter
public class MobAttackInfo {
    private int objectID;
    private byte idk1;
    private byte idk2;
    private byte idk3;
    private byte idk4;
    private byte idk5;
    private byte calcDamageStatIndex;
    private short rcDstX;
    private short rectRight;
    private short oldPosX;
    private short oldPosY;
    private short hpPerc;
    private long[] damages;
    private int mobUpDownYRange;
    private byte type;
    private String currentAnimationName;
    private int animationDeltaL;
    private String[] hitPartRunTimes;
    private int templateID;
    private short idk6;
    private boolean isResWarriorLiftPress;
    private Position pos1;
    private Position pos2;
    private Rect rect;
    private int idkInt;
    private byte byteIdk1;
    private byte byteIdk2;
    private byte byteIdk3;
    private byte byteIdk4;
    private byte byteIdk5;
    private int psychicLockInfo;
    private byte rocketRushInfo;
}
