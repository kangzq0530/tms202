package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/6/1.
 */
public enum ContiState {
    CONTI_DORMANT(0),
    CONTI_WAIT(1),
    CONTI_START(2),
    CONTI_MOVE(3),
    CONTI_MOB_GEN(4),
    CONTI_MOB_DESTROY(5),
    CONTI_END(6),
    CONTI_TARGET_START_FIELD(7),
    CONTI_TARGET_START_SHIP_MOVE_FIELD(8),
    CONTI_TARGET_MOVE_FIELD(10),
    CONTI_TARGET_END_SHIP_MOVE_FIELD(12),
    ;

    @Getter
    private int value;

    ContiState(int value) {
        this.value = value;
    }
}
