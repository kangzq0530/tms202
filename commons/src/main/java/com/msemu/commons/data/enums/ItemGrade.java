package com.msemu.commons.data.enums;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/13.
 */
public enum ItemGrade {
    HIDDEN_LEGENDARY(4),
    HIDDEN_UNIQUE(3),
    HIDDEN_EPIC(2),
    HIDDEN_RARE(1),
    HIDDEN(-1),
    NONE(0),
    // 特殊
    RARE(17),
    // 稀有
    EPIC(18),
    // 罕見
    UNIQUE(19),
    // 傳說
    LEGENDARY(20),
    ;

    private int value;

    ItemGrade(int val) {
        this.value = val;
    }

    public short getValue() {
        return (short) value;
    }

    public static ItemGrade getGradeByVal(int grade) {
        return Arrays.stream(values()).filter(ig -> ig.getValue() == grade).findFirst().orElse(null);
    }

    public static ItemGrade getGradeByOption(int option) {
        ItemGrade itemGrade = NONE;
        if(option < 0) {
            itemGrade =  Arrays.stream(values()).filter(is -> is.getValue() == Math.abs(option)).findFirst().orElse(NONE);
        }
        if(option > 0 && option < 20000) {
            itemGrade = RARE;
        }
        if(option > 20000 && option < 30000) {
            itemGrade = EPIC;
        }
        if(option > 30000 && option < 40000) {
            itemGrade = UNIQUE;
        }
        if(option > 40000 && option < 60000) {
            itemGrade = LEGENDARY;
        }
        return itemGrade;
    }

    public static boolean isMatching(short first, short second) {
        ItemGrade firstGrade = getGradeByVal(first);
        ItemGrade other = getGradeByVal(second);
        switch(firstGrade) {
            case NONE:
                return other == NONE;
            case HIDDEN_RARE:
            case RARE:
                return other == HIDDEN_RARE || other == RARE;
            case HIDDEN_EPIC:
            case EPIC:
                return other == HIDDEN_EPIC || other == EPIC;
            case HIDDEN_UNIQUE:
            case UNIQUE:
                return other == HIDDEN_UNIQUE || other == UNIQUE;
            case HIDDEN_LEGENDARY:
            case LEGENDARY:
                return other == HIDDEN_LEGENDARY || other == LEGENDARY;
            default:
                return false;
        }
    }

    public static ItemGrade getHiddenGradeByValue(short value) {
        ItemGrade ig = NONE;
        ItemGrade arg = getGradeByVal(value);
        switch(arg) {
            case RARE:
                ig = HIDDEN_RARE;
                break;
            case EPIC:
                ig = HIDDEN_EPIC;
                break;
            case UNIQUE:
                ig = HIDDEN_UNIQUE;
                break;
            case LEGENDARY:
                ig = HIDDEN_LEGENDARY;
                break;
        }
        return ig;
    }
}

