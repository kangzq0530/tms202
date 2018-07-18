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

    // 潛能機率
    public static final int RANDOM_EQUIP_UNIQUE_CHANCE = 1; // out of a 100
    public static final int RANDOM_EQUIP_EPIC_CHANCE = 3; // out of a 100
    public static final int RANDOM_EQUIP_RARE_CHANCE = 8; // out of a 100
    public static final int THIRD_LINE_CHANCE = 50;

    public static long[] CHAR_EXP_TABLE = new long[251];

    // Quest Store
    public static final int QUESTID_QUICK_SLOT = 123000;


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
        for (int i = 105; i <= 159; i++) CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.07);
        for (int i = 160; i <= 199; i++) CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.06);
        CHAR_EXP_TABLE[200] = CHAR_EXP_TABLE[199] * 2;
        for (int i = 201; i <= 209; i++) CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.2);
        CHAR_EXP_TABLE[210] = (long) (CHAR_EXP_TABLE[209] * 1.06 * 2);
        for (int i = 211; i <= 219; i++) CHAR_EXP_TABLE[i] = (long) (CHAR_EXP_TABLE[i - 1] * 1.06);
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

    public static double getJobDamageConst(int nJob) {
        if (nJob > 222) {
            if (nJob > 1200) {
                if (nJob >= 1210 && nJob <= 1212)
                    return 0.2;
            } else if (nJob == 1200 || nJob >= 230 && nJob <= 232) {
                return 0.2;
            }
            return 0.0;
        }
        if (nJob < 220) {
            switch (nJob) {
                case 110:
                case 111:
                case 112:
                    return 0.1;
                case 200:
                case 210:
                case 211:
                case 212:
                    return 0.2;
                default:
                    return 0.0;
            }
        }
        return 0.2;
    }

    public static int calcBaseDamage(int p1, int p2, int p3, int ad, double k, boolean bPvP) {
        double result; // eax
        result = ((p3 + p2 + 4 * p1) / 100.0 * (ad * k) + 0.5);
        if (bPvP)
            return (int) Math.log10(result);
        return (int) result;
    }

    public static int calcHybridBaseDamage(int p1, int p2, int p3, int p4, int ad, double k, boolean bPvP) {
        int result = (int) ((p1 * 3.5 + p2 * 3.5 + 3.5 * p3 + p4) / 100.0 * (ad * k) + 0.5);
        if (bPvP)
            return (int) Math.log10(result);
        return result;
    }

    public static double getRand(long rand, double min, double max) {
        double swap;
        if (min > max) {
            swap = min;
            min = max;
            max = swap;
        }
        return min + (rand % 10000000L) * (max - min) / 9999999.0;
    }

    public static double adjustRandomDamage(double damage, int rand, double k, int mastery, double limitedMastery) {
        double v6 = Math.min(mastery / 100.0 + k, limitedMastery) * damage + 0.5;
        double v5 = damage;
        if (v5 > v6) {
            double swap = v5;
            v5 = v6;
            v6 = v5;
        }
        if (v6 != v5) {
            return (rand % 10000000) * (v6 - v5) / 9999999.0 + v5;
        }
        return damage;
    }

    public static double getMultiKillExpRate(int multiKillCount, boolean bossKilled) {
        double x = 0;
        switch (multiKillCount) {
            case 3:
                x = 0.030D;
                break;
            case 4:
                x = 0.080D;
                break;
            case 5:
                x = 0.1525D;
                break;
            case 6:
                x = 0.198D;
                break;
            case 7:
                x = 0.252D;
                break;
            case 8:
                x = 0.312D;
                break;
            case 9:
                x = 0.378D;
                break;
            case 10:
                x = 0.45D;
                break;
            case 11:
                x = 0.495D;
                break;
            case 12:
                x = 54D;
                break;
            case 13:
                x = 58.5D;
                break;
            case 14:
                x = 0.63D;
                break;
            default:
                if (multiKillCount >= 15) {
                    x = 0.675D;
                } else {
                    x = 0.0D;
                }
                break;
        }
        if(bossKilled)
            x /= multiKillCount;
        return x;
    }
}
