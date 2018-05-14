package com.msemu.world.constants;

/**
 * Created by Weber on 2018/4/11.
 */
public class GameConstants {
    public static final long MAX_MONEY = 10_000_000_000L;
    public static final int MAX_HP = 500000;
    public static final int MAX_MP = 500000;
    public static final int MAX_LEVEL = 255;
    public static final int MAX_BASIC_STAT = 32767;
    public static final int DROP_HEIGHT = 20;
    public static final int DROP_DIFF = 25;
    public static long[] CHAR_EXP_TABLE = new long[251];

    static {
        initCharExp();
    }

    public static int maxViewRangeSq() {
//        return 1366 * 768 * 2;//1000000; // 1024 * 768
        return Integer.MAX_VALUE;
    }

    public static int maxViewRangeSq_Half() {
//        return 1366 * 768;//500000; // 800 * 800
        return Integer.MAX_VALUE;
    }

    private static void initCharExp() {
        // NEXTLEVEL::NEXTLEVEL
        CHAR_EXP_TABLE[1] = 15;
        CHAR_EXP_TABLE[2] = 32;
        CHAR_EXP_TABLE[3] = 57;
        CHAR_EXP_TABLE[4] = 92;
        CHAR_EXP_TABLE[5] = 135;
        CHAR_EXP_TABLE[6] = 372;
        CHAR_EXP_TABLE[7] = 560;
        CHAR_EXP_TABLE[8] = 840;
        CHAR_EXP_TABLE[9] = 1242;
        for (int i = 10; i <= 14; i++) {
            CHAR_EXP_TABLE[i] = CHAR_EXP_TABLE[i - 1];
        }
        for (int i = 15; i <= 29; i++) {
            CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.2);
        }
        for (int i = 30; i <= 34; i++) {
            CHAR_EXP_TABLE[i] = CHAR_EXP_TABLE[i - 1];
        }
        for (int i = 35; i <= 39; i++) {
            CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.2);
        }
        for (int i = 40; i <= 59; i++) {
            CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.08);
        }
        for (int i = 60; i <= 64; i++) {
            CHAR_EXP_TABLE[i] = CHAR_EXP_TABLE[i - 1];
        }
        for (int i = 65; i <= 74; i++) {
            CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.08);
        }
        for (int i = 75; i <= 99; i++) {
            CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.07);
        }
        for (int i = 100; i <= 104; i++) {
            CHAR_EXP_TABLE[i] = CHAR_EXP_TABLE[i - 1];
        }
        for (int i = 105; i <= 159; i++) {
            CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.07);
        }
        for (int i = 160; i <= 199; i++) {
            CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.06);
        }
        CHAR_EXP_TABLE[200] = CHAR_EXP_TABLE[199] * 2;
        for (int i = 201; i <= 209; i++) {
            CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.2);
        }
        CHAR_EXP_TABLE[210] = (long) (CHAR_EXP_TABLE[209] * 1.06 * 2);
        for (int i = 211; i <= 219; i++) {
            CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.06);
        }
        CHAR_EXP_TABLE[220] = (long) (CHAR_EXP_TABLE[219] * 1.04 * 2);
        for (int i = 221; i <= 229; i++) {
            CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.04);
        }
        CHAR_EXP_TABLE[230] = (long) (CHAR_EXP_TABLE[229] * 1.02 * 2);
        for (int i = 231; i <= 239; i++) {
            CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.02);
        }
        CHAR_EXP_TABLE[240] = (long) (CHAR_EXP_TABLE[239] * 1.01 * 2);
        for (int i = 241; i <= 249; i++) {
            CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.01);
        }
    }

    public static boolean isBanBanBaseField(int dwFieldID) {
        return (dwFieldID / 0xA) == 10520011 || (dwFieldID / 0xA) == 10520051 || dwFieldID == 105200519;
    }

    public static int getHyperStatReqAp(int level) {
        switch (level) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 8;
            case 5:
                return 10;
            case 6:
                return 15;
            case 7:
                return 20;
            case 8:
                return 25;
            case 9:
                return 30;
            case 10:
                return 35;
            default:
                return 0;
        }
    }
}
