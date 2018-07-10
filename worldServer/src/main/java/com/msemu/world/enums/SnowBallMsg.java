package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/6/1.
 */
public enum SnowBallMsg {
    SECTION0(0),
    SECTION1(1),
    SECTION2(2),
    SECTION3(3),
    STOP(4),
    RESTART(5),
    ;

    @Getter
    private int value;

    SnowBallMsg(int value) {
        this.value = value;
    }
}
