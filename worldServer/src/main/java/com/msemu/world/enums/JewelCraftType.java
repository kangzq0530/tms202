package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/3.
 */
public enum JewelCraftType {
    InstallSuccess(0),
    InstallFail(1),
    SynthesizeSuccess(2),
    SynthesizeKeep(3),
    SynthesizeFail(4),
    Error(5),
    ;
    @Getter
    private byte value;

    JewelCraftType(int value) {
        this.value = (byte) value;
    }
}
