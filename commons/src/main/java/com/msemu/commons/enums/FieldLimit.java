package com.msemu.commons.enums;

/**
 * Created by Weber on 2018/5/21.
 */
public enum FieldLimit {
    Jump(0),
    UseSkill(1),
    SummonItem(2),
    MysticDoor(3),
    Migrate(4),
    PortalScroll(5),
    UseTeleportItem(6),
    OpenMiniGame(7),
    UseSpecificPortalScroll(8),
    UseTamingMob(9),
    ConsumeStatChangeItem(10),
    ChangePartyBoss(11),
    UseWeddingInvitationItem(13),
    UseCashWeatherItem(14),
    UsePet(15),
    FallDown(17),
    SummonNpc(18),
    ExpLose(20),;
    private int value;

    FieldLimit(int index) {
        this.value = 1 << index;
    }

    public boolean isLimit(int fieldLimit) {
        return (this.value & fieldLimit) > 0;
    }
}
