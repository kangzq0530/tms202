package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/15.
 */
public enum NpcMessageParam {

    NONE(0),
    NO_CLOSE(1),
    SHOW_CHAR_ON_RIGHT_SIDE(2),
    NPC_ON_LEFT_SIDE_OVERRIDE(4),
    NPC_ON_LEFT_SIDE_TURN_RIGHT(8),
    CHAR_ON_RIGHT_SIDE_TURN_RIGHT(16),
    BOTTOM_DIALOG_WITH_DELAY(32),;
    @Getter
    private int value;

    NpcMessageParam(int value) {
        this.value = value;
    }
}
