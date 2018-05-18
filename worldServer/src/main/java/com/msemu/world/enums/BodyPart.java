package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/13.
 */
public enum BodyPart {
    HAT(1),
    FACE(2),
    EYES(3),
    EARRINGS(4),
    TOP(5), // Includes overall
    PANTS(6),
    BOOTS(7),
    GLOVES(8),
    CAPE(9),
    SHIELD(10), // Includes things such as katara, 2ndary
    WEAPON(11),
    RING_1(12),
    RING_2(13),
    PET_EQUIP_1(14),
    RING_3(15),
    RING_4(16),
    NECKLACE(17),
    MOUNT(18),
    MOUNT_COVER(19),
    MEDAL(21),
    BELT(22),
    SHOULDER(23),
    PET_EQUIP_2(24),
    PET_EQUIP_3(25),
    POCKET(26),
    ANDROID(27),
    HEART_1(28),
    BADGE(29),
    EMBLEM(30),
    MECH_ENGINE(1100),
    MECH_ARM(1101),
    MECH_LEG(1102),
    MECH_FRAME(1103),
    MECH_TRANSISTOR(1104),
    NECKLACE_CASH(31),
    BITS(1400), // 1400~1424
    ;

    private int value;

    BodyPart(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
