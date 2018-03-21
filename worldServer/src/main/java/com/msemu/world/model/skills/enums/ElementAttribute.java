package com.msemu.world.model.skills.enums;

/**
 * Created by Weber on 2018/3/21.
 */
public enum ElementAttribute {

    NEUTRAL(0), PHYSICAL(1), FIRE(2, true), ICE(3, true), LIGHTING(4), POISON(5), HOLY(6, true), DARKNESS(7);

    private final int value;
    private boolean special = false;

    private ElementAttribute(int v) {
        this.value = v;
    }

    private ElementAttribute(int v, boolean special) {
        this.value = v;
        this.special = special;
    }

    public boolean isSpecial() {
        return special;
    }

    public static ElementAttribute fromString(String elemAttr) {
        switch (elemAttr.toUpperCase()) {
            case "F":
                return FIRE;
            case "I":
                return ICE;
            case "L":
                return LIGHTING;
            case "S":
                return POISON;
            case "H":
                return HOLY;
            case "P":
                return PHYSICAL;
            case "D":
                return DARKNESS;
            case "":
                return NEUTRAL;
        }
        throw new IllegalArgumentException("未知的技能屬性: " + elemAttr);
    }

    public static ElementAttribute getFromId(int elementID) {
        for (ElementAttribute e : ElementAttribute.values()) {
            if (e.value == elementID) {
                return e;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
