package com.msemu.world.enums;

import lombok.Getter;

@Getter
public enum EnchantScroll {

    武器_100("武器攻擊力卷軸", 100, 0, 160, 4, 3),
    武器_70("武器攻擊力卷軸", 70, 1, 200, 5, 4),
    武器_30("武器攻擊力卷軸", 30, 2, 250, 6, 5),
    武器_15("武器攻擊力卷軸", 15, 3, 320, 8, 7),
    攻擊力_100("攻擊力卷軸", 100, 0, 160, 1, 0),
    攻擊力_70("攻擊力卷軸", 70, 1, 200, 2, 0),
    攻擊力_30("攻擊力卷軸", 30, 2, 250, 3, 0),
    攻擊力_15("攻擊力卷軸", 15, 3, 300, 4, 0),
    回真卷軸("回真卷軸", 30, 4, 5000, 0, 0),
    純白的咒文書("純白的咒文書", 5, 5, 2000, 0, 0),;

    private final String name;
    private final int successRate;
    private final int viewType;
    private final int cost;
    private final int mask;
    private final int atk;
    private final int stat;

    EnchantScroll(String name, int successRate, int viewType, int cost, int atk, int stat) {
        this.name = name;
        this.successRate = successRate;
        this.viewType = viewType;
        this.cost = cost;
        this.atk = atk;
        this.stat = stat;
        this.mask = (atk <= 0 ? 0 : (EnchantStat.PAD.getValue() | EnchantStat.MAD.getValue())) | (stat <= 0 ? 0 : (EnchantStat.STR.getValue() | EnchantStat.DEX.getValue() | EnchantStat.INT.getValue() | EnchantStat.LUK.getValue()));
    }

}
