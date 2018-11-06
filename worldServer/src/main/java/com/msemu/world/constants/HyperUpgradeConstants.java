/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.world.constants;

import com.msemu.commons.data.enums.EnchantStat;
import com.msemu.world.client.character.inventory.items.Equip;

import java.util.EnumMap;
import java.util.Map;

public class HyperUpgradeConstants {


    public  static final int MINIGAME_LEVEL = 0;

    // 成功機率 (1000為底)
    private static final int[] NORMAL_PROP_UPGRADE_SUCCESS = {
            950, 900, 850, 850, 800, 750, 700, 650, 600, 550,
            450, 350, 300, 300, 300, 300, 300, 300, 300, 300,
            300, 300, 30, 20, 10};
    // 破壞機率 (1000為底)
    private static final int[] NORMAL_PROP_UPGRADE_DESTROY = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 200, 350, 400, 450, 600, 650, 750, 800, 850,
            900, 950, 1000, 1000, 1000};
    // 尊榮裝備成功機率 (1000為底)
    private static final int[] SUPERIOR_PROP_UPGRADE_SUCCESS = {
            500, 500, 450, 400, 400, 400, 400, 400, 400,
            370, 350, 350, 30, 20, 10
    };
    // 尊榮裝備破壞機率 (1000為底)
    private static final int[] SUPERIOR_PROP_UPGRADE_DESTROY = {
            0, 0, 0, 0, 0, 60, 100, 140, 200, 300,
            400, 500, 1000, 1000, 1000
    };

    // 消耗楓幣
    private static final long[] COST_LEVEL_100 = {41000, 81000, 121000, 161000, 201000, 241000, 281000, 321000};
    private static final long[] COST_LEVEL_110 = {54200, 107500, 160700, 214000, 267200, 320400, 373700, 426900, 480200, 533400};
    private static final long[] COST_LEVEL_120 = {70100, 139200, 208400, 277500, 346600, 415700, 484800, 554000, 623100, 692200, 5602100, 7085400, 8794500, 10742400, 12941800};
    private static final long[] COST_LEVEL_130 = {88900, 176800, 264600, 352500, 440400, 528300, 616200, 704000, 791900, 879800, 7122300, 9008200, 11181100, 13657700, 16454100, 19586000, 23069100, 26918600, 31149300, 35776100};
    private static final long[] COST_LEVEL_140 = {110800, 220500, 330300, 440000, 549800, 659600, 769300, 879100, 988800, 1098600, 8895400, 11250800, 13964700, 17057900, 20550500, 24462200, 28812500, 33620400, 38904500, 44683300, 50974700, 57796700, 65166700, 73102200, 81620200};
    private static final long[] COST_LEVEL_150 = {136000, 271000, 406000, 541000, 676000, 811000, 946000, 1081000, 1216000, 1351000, 10940700, 13837700, 17175800, 20980200
            , 25275900, 30087200, 35437900, 41351400, 47850600, 54985200, 62696400
            , 71087200, 80152000, 89912300, 100389000};
    private static final long[] COST_LEVEL_200 = {536996, 1070000, 1603000, 1713000, 2140000, 2567000, 2994000, 3421000, 3848229, 4275000, 34619000, 43785800, 54348348, 66386300
            , 79978900, 95202900, 112133700, 130845300, 151410200, 173985699, 198385600
            , 224935900, 253618900, 284502600, 317653218};
    private static final long[] COST_SUPERIOR = {55832200, 55832200, 55832200, 55832200, 55832200, 55832200};


    private static final int[] SUPERIOR_INC_STATS_LEVEL_80 = {2, 3, 5};
    private static final int[] SUPERIOR_INC_STATS_LEVEL_110 = {9, 11, 13, 15, 17, 0, 0, 0};
    private static final int[] SUPERIOR_INC_STATS_LEVEL_150 = {19, 20, 22, 25, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final int[] SUPERIOR_INC_AD_LEVEL_80 = {0, 0, 0};
    private static final int[] SUPERIOR_INC_AD_LEVEL_110 = {0, 0, 0, 0, 0, 5, 6, 7};
    private static final int[] SUPERIOR_INC_AD_LEVEL_150 = {0, 0, 0, 0, 0, 9, 10, 11, 12, 13, 15, 17, 19, 21, 23};

    private static final int[] NORMAL_INC_MAXHPMP = {
            5, 5, 5, 10, 10,
            15, 15, 15, 20, 25, 25, 25, 25, 25, 30,
            0, 0, 0, 0, 0, 0, 0,
            0, 0, 0
    };

    private static class Level140 {
        private static final int[] NORMAL_INC_STATS = {
                2, 2, 2, 2, 2,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                9, 9, 9, 9, 9, 9, 9,
                0, 0, 0
        };
        private static final int[] NORMAL_INC_AD = {
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                8, 9, 10, 11, 12, 13, 15,
                17, 0, 0
        };
        private static final int[] GLOVE_INC_STATS = {
                2, 2, 2, 2, 2,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                9, 9, 9, 9, 9, 9, 9,
                0, 0, 0
        };
        private static final int[] GLOVE_INC_AD = {
                0, 0, 0, 0, 1,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                8, 9, 10, 11, 12, 13, 15,
                17, 0, 0
        };
        private static final int[] WEAPON_INC_STATS = {
                2, 2, 2, 2, 2,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                9, 9, 9, 9, 9, 9, 9,
                0, 0, 0
        };
        private static final int[] WEAPON_INC_AD = {
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                8, 8, 9, 9, 10, 11, 12,
                30, 0, 0
        };
    }

    private static class Level150 {

        private static final int[] NORMAL_INC_STATS = {
                2, 2, 2, 2, 2,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                11, 11, 11, 11, 11, 11, 11,
                0, 0, 0
        };
        private static final int[] NORMAL_INC_AD = {
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                9, 10, 11, 12, 13, 14, 16,
                18, 20, 22
        };
        private static final int[] GLOVE_INC_STATS = {
                2, 2, 2, 2, 2,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                11, 11, 11, 11, 11, 11, 11,
                0, 0, 0
        };
        private static final int[] GLOVE_INC_AD = {
                0, 0, 0, 0, 1,
                0, 1, 0, 1, 0, 1, 0, 1, 0, 2,
                9, 10, 11, 12, 13, 14, 16,
                18, 20, 22
        };
        private static final int[] WEAPON_INC_STATS = {
                2, 2, 2, 2, 2,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                11, 11, 11, 11, 11, 11, 11,
                0, 0, 0
        };
        private static final int[] WEAPON_INC_AD = {
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                8, 9, 9, 10, 11, 12, 13,
                31, 0, 0
        };
    }

    private static class Level160 {
        private static final int[] NORMAL_INC_STATS = {
                2, 2, 2, 2, 2,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                13, 13, 13, 13, 13, 13, 13,
                0, 0, 0
        };
        private static final int[] NORMAL_INC_AD = {
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                10, 11, 12, 13, 14, 15, 17,
                19, 21, 23
        };
        private static final int[] GLOVE_INC_STATS = {
                2, 2, 2, 2, 2,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                13, 13, 13, 13, 13, 13, 13,
                0, 0, 0
        };
        private static final int[] GLOVE_INC_AD = {
                0, 0, 0, 0, 1,
                0, 1, 0, 1, 0, 1, 0, 1, 0, 2,
                10, 11, 12, 13, 14, 15, 17,
                19, 21, 23
        };
        private static final int[] WEAPON_INC_STATS = {
                2, 2, 2, 2, 2,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                13, 13, 13, 13, 13, 13, 13,
                0, 0, 0
        };
        private static final int[] WEAPON_INC_AD = {
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                9, 9, 10, 11, 12, 13, 14,
                32, 34, 36
        };
    }

    private static class Level200 {
        private static final int[] NORMAL_INC_STATS = {
                2, 2, 2, 2, 2,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                15, 15, 15, 15, 15, 15, 15,
                0, 0, 0
        };
        private static final int[] NORMAL_INC_AD = {
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                12, 13, 14, 15, 16, 17, 19,
                21, 23, 25
        };
        private static final int[] GLOVE_INC_STATS = {
                2, 2, 2, 2, 2,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                15, 15, 15, 15, 15, 15, 15,
                0, 0, 0
        };
        private static final int[] GLOVE_INC_AD = {
                0, 0, 0, 0, 1,
                0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
                12, 13, 14, 15, 16, 17, 19,
                21, 23, 25
        };
        private static final int[] WEAPON_INC_STATS = {
                2, 2, 2, 2, 2,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                15, 15, 15, 15, 15, 15, 15,
                0, 0, 0
        };
        private static final int[] WEAPON_INC_AD = {
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                13, 13, 14, 14, 15, 16, 17,
                34, 36, 38
        };
    }


    public static long getHyperUpgradeCost(int level, int chuc, boolean superior) {
        if (superior) {
            if (level >= 0 && level <= 109) {
                return HyperUpgradeConstants.COST_SUPERIOR[0];
            } else if (level >= 110 && level <= 119) {
                return HyperUpgradeConstants.COST_SUPERIOR[1];
            } else if (level >= 120 && level <= 129) {
                return HyperUpgradeConstants.COST_SUPERIOR[2];
            } else if (level >= 130 && level <= 139) {
                return HyperUpgradeConstants.COST_SUPERIOR[3];
            } else if (level >= 140 && level <= 149) {
                return HyperUpgradeConstants.COST_SUPERIOR[4];
            } else {
                return HyperUpgradeConstants.COST_SUPERIOR[5];
            }
        } else {
            if (level >= 0 && level <= 109) {
                return HyperUpgradeConstants.COST_LEVEL_100[chuc];
            } else if (level >= 110 && level <= 119) {
                return HyperUpgradeConstants.COST_LEVEL_110[chuc];
            } else if (level >= 120 && level <= 129) {
                return HyperUpgradeConstants.COST_LEVEL_120[chuc];
            } else if (level >= 130 && level <= 139) {
                return HyperUpgradeConstants.COST_LEVEL_130[chuc];
            } else if (level >= 140 && level <= 149) {
                return HyperUpgradeConstants.COST_LEVEL_140[chuc];
            } else if (level >= 150 && level <= 199) {
                return HyperUpgradeConstants.COST_LEVEL_150[chuc];
            } else {
                return HyperUpgradeConstants.COST_LEVEL_200[chuc];
            }
        }
    }


    public static int getHyperSuccessProp(int chuc, boolean superior) {
        return superior ? SUPERIOR_PROP_UPGRADE_SUCCESS[chuc] : NORMAL_PROP_UPGRADE_SUCCESS[chuc];
    }

    public static int getHyperDestroyProp(int chuc, boolean superior) {
        return superior ? SUPERIOR_PROP_UPGRADE_DESTROY[chuc] : NORMAL_PROP_UPGRADE_DESTROY[chuc];
    }


    public static Map<EnchantStat, Integer> getEnchantStats(Equip equip) {
        return getEnchantStats(equip, equip.getChuc(), equip.getChuc() + 1);
    }

    public static Map<EnchantStat, Integer> getEnchantStats(Equip equip, int chucFrom, int chucTo) {
        final int itemId = equip.getItemId();
        final int reqLevel = equip.getTemplate().getReqLevel();
        final int reqJob = equip.getTemplate().getRJob();
        final boolean superior = equip.getTemplate().isSuperiorEqp();
        final boolean glove = ItemConstants.類型.手套(itemId);
        final boolean weapon = ItemConstants.類型.武器(itemId) || ItemConstants.類型.雙刀(itemId);
        final boolean cape = ItemConstants.類型.披風(itemId);
        final boolean shoulder = ItemConstants.類型.肩飾(itemId);
        final Map<EnchantStat, Integer> stats = new EnumMap<>(EnchantStat.class);
        int iAllStats = 0;
        int iPad = 0;
        int iMad = 0;
        int iPdd;
        int iMdd;
        int iHMP = 0;

        for (int chuc = chucFrom; chuc < chucTo; chuc++) {

            if (superior) {
                switch (reqLevel) {
                    case 80:
                        iMad += SUPERIOR_INC_AD_LEVEL_80[chuc];
                        iPad += SUPERIOR_INC_AD_LEVEL_80[chuc];
                        iAllStats += SUPERIOR_INC_STATS_LEVEL_80[chuc];
                        break;
                    case 110:
                        iMad += SUPERIOR_INC_AD_LEVEL_110[chuc];
                        iPad += SUPERIOR_INC_AD_LEVEL_110[chuc];
                        iAllStats = SUPERIOR_INC_STATS_LEVEL_110[chuc];
                        break;
                    case 150:
                        iMad += SUPERIOR_INC_AD_LEVEL_150[chuc];
                        iPad += SUPERIOR_INC_AD_LEVEL_150[chuc];
                        iAllStats += SUPERIOR_INC_STATS_LEVEL_150[chuc];
                        break;
                }
            } else {
                if (glove) {
                    if (reqLevel <= 149) {
                        iAllStats += Level140.GLOVE_INC_STATS[chuc];
                        iMad += Level140.GLOVE_INC_AD[chuc];
                        iPad += Level140.GLOVE_INC_AD[chuc];
                    } else if (reqLevel <= 159) {
                        iAllStats += Level150.GLOVE_INC_STATS[chuc];
                        iMad += Level150.GLOVE_INC_AD[chuc];
                        iPad += Level150.GLOVE_INC_AD[chuc];
                    } else if (reqLevel <= 199) {
                        iAllStats += Level160.GLOVE_INC_STATS[chuc];
                        iMad += Level160.GLOVE_INC_AD[chuc];
                        iPad += Level160.GLOVE_INC_AD[chuc];
                    } else {
                        iAllStats += Level200.GLOVE_INC_STATS[chuc];
                        iMad += Level200.GLOVE_INC_AD[chuc];
                        iPad += Level200.GLOVE_INC_AD[chuc];
                    }
                } else if (weapon) {
                    if (reqLevel <= 149) {
                        iAllStats += Level140.WEAPON_INC_STATS[chuc];
                        iMad += Level140.WEAPON_INC_AD[chuc];
                        iPad += Level140.WEAPON_INC_AD[chuc];
                    } else if (reqLevel <= 159) {
                        iAllStats += Level150.WEAPON_INC_STATS[chuc];
                        iMad += Level150.WEAPON_INC_AD[chuc];
                        iPad += Level150.WEAPON_INC_AD[chuc];
                    } else if (reqLevel <= 199) {
                        iAllStats += Level160.WEAPON_INC_STATS[chuc];
                        iMad += Level160.WEAPON_INC_AD[chuc];
                        iPad += Level160.WEAPON_INC_AD[chuc];
                    } else {
                        iAllStats += Level200.WEAPON_INC_STATS[chuc];
                        iMad += Level200.WEAPON_INC_AD[chuc];
                        iPad += Level200.WEAPON_INC_AD[chuc];
                    }

                } else {
                    if (reqLevel <= 149) {
                        iAllStats += Level140.NORMAL_INC_STATS[chuc];
                        iMad += Level140.NORMAL_INC_AD[chuc];
                        iPad += Level140.NORMAL_INC_AD[chuc];
                    } else if (reqLevel <= 159) {
                        iAllStats += Level150.NORMAL_INC_STATS[chuc];
                        iMad += Level150.NORMAL_INC_AD[chuc];
                        iPad += Level150.NORMAL_INC_AD[chuc];
                    } else if (reqLevel <= 199) {
                        iAllStats += Level160.NORMAL_INC_STATS[chuc];
                        iMad += Level160.NORMAL_INC_AD[chuc];
                        iPad += Level160.NORMAL_INC_AD[chuc];
                    } else {
                        iAllStats += Level200.NORMAL_INC_STATS[chuc];
                        iMad += Level200.NORMAL_INC_AD[chuc];
                        iPad += Level200.NORMAL_INC_AD[chuc];
                    }
                }

                iHMP += NORMAL_INC_MAXHPMP[chuc];
            }


            if ((reqJob & 0x1) != 0) { // 劍士
                stats.put(EnchantStat.STR, iAllStats);
                stats.put(EnchantStat.DEX, iAllStats);
            }
            if ((reqJob & 0x2) != 0) { // 法師
                stats.put(EnchantStat.INT, iAllStats);
                stats.put(EnchantStat.LUK, iAllStats);
            }
            if ((reqJob & 0x4) != 0) { // 弓箭手
                stats.put(EnchantStat.DEX, iAllStats);
                stats.put(EnchantStat.STR, iAllStats);
            }
            if ((reqJob & 0x8) != 0) { // 盜賊
                stats.put(EnchantStat.LUK, iAllStats);
                stats.put(EnchantStat.DEX, iAllStats);
            }
            if ((reqJob & 0x10) != 0) { // 海盜
                stats.put(EnchantStat.STR, iAllStats);
                stats.put(EnchantStat.DEX, iAllStats);
            }
            if (reqJob <= 0 || superior) { // 全職 && 初心者
                stats.put(EnchantStat.STR, iAllStats);
                stats.put(EnchantStat.DEX, iAllStats);
                stats.put(EnchantStat.INT, iAllStats);
                stats.put(EnchantStat.LUK, iAllStats);

            }

            // 武器16星之後全屬性
            if ((weapon || cape || shoulder) && chuc >= 15) {
                stats.put(EnchantStat.STR, iAllStats);
                stats.put(EnchantStat.DEX, iAllStats);
                stats.put(EnchantStat.INT, iAllStats);
                stats.put(EnchantStat.LUK, iAllStats);
            }

            if (!superior) {
                stats.put(EnchantStat.MHP, iHMP);
                stats.put(EnchantStat.MMP, iHMP);
            }
        }

        // 武器攻擊另外算
        // 防禦另外算

        int beforePAD = equip.getIPad();
        int beforeMAD = equip.getIMad();
        int afterPAD = equip.getIPad();
        int afterMAD = equip.getIMad();
        int beforePDD = equip.getIPDD();
        int beforeMDD = equip.getIMDD();
        int afterPDD = equip.getIPDD();
        int afterMDD = equip.getIMDD();
        for (int i = 0; i < chucTo; i++) {
            if (i < chucFrom) {
                beforePAD += (1 + (beforePAD / 50));
                beforeMAD += (1 + (beforeMAD / 50));
                beforePDD += (1 + (beforePDD / 20));
                beforeMDD += (1 + (beforeMDD / 20));
            }
            afterPAD += (1 + (beforePAD / 50));
            afterMAD += (1 + (beforeMAD / 50));
            afterPDD += (1 + (beforePDD / 20));
            afterMDD += (1 + (beforeMDD / 20));
        }
        if (weapon) {
            iPad += (afterPAD - beforePAD);
            iMad += (afterMAD - beforeMAD);
        }
        iPdd = afterPDD - beforePDD;
        iMdd = afterMDD - beforeMDD;

        stats.put(EnchantStat.PAD, iPad);
        stats.put(EnchantStat.MAD, iMad);
        stats.put(EnchantStat.PDD, iPdd);
        stats.put(EnchantStat.MDD, iMdd);

        return stats;
    }
}
