package com.msemu.commons.enums;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/12.
 */
public enum InvType {

    EQUIPPED(-1),
    EQUIP(1),
    CONSUME(2),
    INSTALL(3),
    ETC(4),
    CASH(5);

    private byte value;

    InvType(int value) {
        this((byte) value);
    }

    InvType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static InvType getInvTypeByVal(int val) {
        return Arrays.stream(InvType.values()).filter(t -> t.getValue() == val).findFirst().orElse(null);
    }

    public static InvType getInvTypeByString(String subMap) {
        subMap = subMap.toLowerCase();
        InvType res = null;
        switch (subMap) {
            case "cash":
            case "pet":
                res = CASH;
                break;
            case "consume":
            case "special":
            case "use":
                res = CONSUME;
                break;
            case "etc":
                res = ETC;
                break;
            case "install":
            case "setup":
                res = INSTALL;
                break;
        }
        return res;
    }

    public boolean isStackable() {
        return this != EQUIP && this != EQUIPPED;
    }
}
