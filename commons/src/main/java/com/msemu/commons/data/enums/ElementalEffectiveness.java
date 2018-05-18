package com.msemu.commons.data.enums;

/**
 * Created by Weber on 2018/4/25.
 */
public enum ElementalEffectiveness {

    NORMAL(1.0), IMMUNE(0.0), STRONG(0.5), WEAK(1.5);

    private final double value;

    private ElementalEffectiveness(final double val) {
        this.value = val;
    }

    public double getValue() {
        return value;
    }

    public static int getNumber(final ElementalEffectiveness elementalEffectiveness) {
        switch (elementalEffectiveness) {
            case NORMAL:
                return 0;
            case IMMUNE:
                return 1;
            case STRONG:
                return 2;
            case WEAK:
                return 3;
        }
        return -1;
    }

    public static ElementalEffectiveness getByNumber(final int num) {
        switch (num) {
            case 0:
                return NORMAL;
            case 1:
                return IMMUNE;
            case 2:
                return STRONG;
            case 3:
                return WEAK;
            default:
                throw new IllegalArgumentException("Unkown effectiveness: " + num);
        }
    }
}

