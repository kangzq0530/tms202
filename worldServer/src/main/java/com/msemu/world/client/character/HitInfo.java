package com.msemu.world.client.character;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/12.
 */
@Getter
@Setter
public class HitInfo {

    private int random;
    private int updateTime;
    private byte type;
    private byte unk;
    private int HPDamage;
    private int templateID;
    private int mobID;
    private int MPDamage;

}
