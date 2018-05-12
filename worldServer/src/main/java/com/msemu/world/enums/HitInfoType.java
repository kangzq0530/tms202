package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/6.
 */
public enum HitInfoType {
    BY_FIELD_ETC(-10),
    BY_SKELETON_BOSS(-9),
    BY_MOB_SKILL(-8),
    BY_HEKATON_FIELD_SKILL(-7),
    BY_OBTACLE_ATOM(-5),

    BY_MOB(-1),
    ;
    @Getter
    private byte type;
    HitInfoType(int type) {
        this.type = (byte) type;
    }
}
