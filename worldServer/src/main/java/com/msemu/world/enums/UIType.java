package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/4/29.
 */
public enum UIType {
    PVP_WINDOW(0x32),
    AZWAN_WINDOW(0x46),
    JOB_CHANGE_UI(0xA4),
    TIME_GATE_WINDOWS(0xA8),
    V_MATRIX_WINDOW(0x46B),
    PROFESSION_WINDOW(42),
    CASSANDRAS_COLLECTION_WINDOW(127),
    BEAST_TAMER_GIFT_WINDOW(194),
    LUCKY_LUCKY_MONSTORY_WINDOW(120),
    REPAIR_WINDOW(0x21),
    JEWEL_CRAFT_WINDOW(0x68),
    RED_LEAF_WINDOW(0x73);
    @Getter
    private int value;

    UIType(int value) {
        this.value = value;
    }
}
