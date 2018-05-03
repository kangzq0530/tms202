package com.msemu.login.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/3.
 */
public enum WorldStatus {
    伺服器人數較多(1),
    伺服器人數已滿(2),
    ;
    @Getter
    private byte value;

    WorldStatus(int value) {
        this.value = (byte) value;
    }
}
