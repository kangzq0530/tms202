package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/4/29.
 */
public enum  UIType {
    PVPWindow(0x32),
    AzwanWindow(0x46),
    JobChangeUI(0xA4),
    TimeGateWindows(0xA8),
    VMatrixWindow(0x46B),
    ProfessionWindow(42),
    CassandrasCollectionWindow(127),
    BeastTamerGiftWindow(194),
    LuckyLuckyMonstoryWindow(120),
    RepairWindow(0x21),
    JewelCraftWindow(0x68),
    RedLeafWindow(0x73)
    ;
    @Getter
    private int value;

    UIType(int value) {
        this.value = value;
    }
}
