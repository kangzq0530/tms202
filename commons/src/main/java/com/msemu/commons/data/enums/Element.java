package com.msemu.commons.data.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/4/25.
 */
public enum Element {

    NEUTRAL(0), PHYSICAL(1), FIRE(2, true), ICE(3, true), LIGHTING(4), POISON(5), HOLY(6, true), DARKNESS(7);

    @Getter
    private final int value;
    private boolean special = false;

    private Element(int v) {
        this.value = v;
    }

    private Element(int v, boolean special) {
        this.value = v;
        this.special = special;
    }

    public boolean isSpecial() {
        return special;
    }

    public static Element getFromChar(String elemAttr) {
        switch (Character.toUpperCase(elemAttr.charAt(0))) {
            case 'F':
                return FIRE;
            case 'I':
                return ICE;
            case 'L':
                return LIGHTING;
            case 'S':
                return POISON;
            case 'H':
                return HOLY;
            case 'P':
                return PHYSICAL;
            case 'D': // Added on v.92 MSEA
                return DARKNESS;
        }
        throw new IllegalArgumentException("unknown elemnt char " + elemAttr);
    }

    public static Element getFromId(int value) {
        for (Element e : Element.values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new IllegalArgumentException("unknown elemnt id " + value);
    }
}
