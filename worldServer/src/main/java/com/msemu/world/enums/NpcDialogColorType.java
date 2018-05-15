package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/15.
 */
public enum NpcDialogColorType {

    BLUE(0x0),
    BROWN(0x01),;
    @Getter
    private int value;

    NpcDialogColorType(int value) {
        this.value = value;
    }
}
