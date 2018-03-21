package com.msemu.world.model.skills.summons.enums;

/**
 * Created by Weber on 2018/3/21.
 */
public enum SummonMovementType {

    不會移動(0),
    飛行跟隨(1),
    自由移動(2),
    跟隨飛行隨機移動攻擊(3),
    盤旋攻擊怪死後跟隨(5),
    移動跟隨(7),
    跟隨移動跟隨攻擊(8),
    美洲豹(11);
    private final int val;

    private SummonMovementType(int val) {
        this.val = val;
    }

    public int getValue() {
        return val;
    }
}
