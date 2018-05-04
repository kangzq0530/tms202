package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/13.
 */
public enum DropLeaveType {
    ByTimeOut(0),
    ByScreenScroll(1),
    PickedUpByUser(2),
    PickUpByMob(3),
    Explode(4),
    PickedUpByPet(5),
    PassConvex(6),
    SkillPet(7), // CAnimationDisplayer::RegisterAbsorbItemAnimationJP ?
    ;

    private byte value;

    DropLeaveType(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
