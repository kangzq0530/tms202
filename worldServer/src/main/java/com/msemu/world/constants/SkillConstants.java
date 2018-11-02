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

import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.WeaponType;
import org.slf4j.LoggerFactory;

/**
 * Created by Weber on 2018/3/31.
 */
public class SkillConstants {

    public static final short LINK_SKILL_1_LEVEL = 70;
    public static final short LINK_SKILL_2_LEVEL = 120;
    public static final short LINK_SKILL_3_LEVEL = 210;



    public static boolean canMasteryApply(int skillID, WeaponType weaponType) {
        switch (skillID) {
            case 101000103: // 璃之力
                return weaponType == WeaponType.璃;
            case 101000203: // 琉之力
                return weaponType == WeaponType.琉;
            case 1100000: // 武器精通
            case 1120003: // 進階鬥氣
                return weaponType == WeaponType.單手劍 || weaponType == WeaponType.單手斧 || weaponType == WeaponType.雙手斧 || weaponType == WeaponType.雙手劍;
            case 110000515: // 武器鍛鍊
                return weaponType == WeaponType.幻獸棍棒;
            case 11100025: // 精準之劍
            case 11100000: // 精準之劍
            case 11120007: // 專家之劍
                return weaponType == WeaponType.單手劍 || weaponType == WeaponType.雙手劍;
            case 1200000: // 武器精通
            case 1220018: // 聖騎士大師
                return weaponType == WeaponType.雙手棍 || weaponType == WeaponType.單手棍 ||
                        weaponType == WeaponType.單手劍 || weaponType == WeaponType.雙手劍;
            case 12100007: // 咒語精通
            case 12100027: // 咒語磨練
            case 12120009: // 魔法真理
                return weaponType == WeaponType.長杖 || weaponType == WeaponType.短杖;
            // TODO 熟練度

        }
        return false;
    }

    public static boolean canBeLearnedBy(int skillID, int job) {
        int skillForJob = skillID / 10000;
        return MapleJob.getJobGrade(skillForJob) <= MapleJob.getJobGrade(job) && MapleJob.isSameJob(job, skillForJob);
    }

    public static boolean is4thJobSkill(int skillID) {
        SkillInfo si = SkillData.getInstance().getSkillInfoById(skillID);
        if (si.getHyper() > 0) {
            return true;
        }
        switch (skillID) {
            case 1120012:
            case 1320011: // 靈魂復仇(10/30)27001100
            case 3110014:
            case 4320005:
            case 4340010:
            case 4340012:
            case 5120011:
            case 5120012:
            case 5220012:
            case 5220014:
            case 5321006:
            case 5720008:
            case 21120011:
            case 21120014:
            case 22171004:
            case 22181004:
            case 23120011:
            case 23120013:
            case 23121008:
            case 33120010:
            case 33121005:
            case 51120000: // 戰鬥大師(15/15)
                return false;
        }

        switch (skillID / 10000) {
            case 2312:
            case 2412:
            case 2217:
            case 2218:
            case 2512:
            case 2712:
            case 3122:
            case 6112:
            case 6512:
                return true;
            case 10100:
                return skillID == 101000101;
            case 10110:
                return (skillID == 101100101) || (skillID == 101100201);
            case 10111:
                return (skillID == 101110102) || (skillID == 101110200) || (skillID == 101110203);
            case 10112:
                return (skillID == 101120104) || (skillID == 101120204);
        }
        if ((si.getMaxLevel() <= 15 && !si.isInvisible() && si.getMasterLevel() <= 0)) {
            return false;
        }
        //龍魔技能
        if (skillID / 10000 >= 2210 && skillID / 10000 <= 2218) {
            return ((skillID / 10000) % 10) >= 7 || si.getMasterLevel() > 0;
        }
        //影武技能
        if (skillID / 10000 >= 430 && skillID / 10000 <= 434) {
            return ((skillID / 10000) % 10) == 4 || si.getMasterLevel() > 0;
        }
        if (skillID == 40020002) {
            return true;
        }
        //冒險家
        return ((skillID / 10000) % 10) == 2 && skillID < 90000000 && !MapleJob.isBeginner(skillID / 10000);
    }

    public static int getSkillBookBySkill(final int skillId) {
        return getSkillBookByJob(skillId / 10000, skillId);
    }

    public static int getSkillJobLevel(int job) {
        return getSkillBookByJob(job, 0);
    }

    public static int getSkillBookByJob(final int job, final int skillId) {
        if (MapleJob.isBeginner(job)) {
            return 0;
        }

        if (MapleJob.is神之子(job)) {
            if (skillId > 0) {
                int type = (skillId % 1000) / 100; //1 beta 2 alpha
                return type == 1 ? 1 : 0;
            } else {
                return 0;
            }
        }

        if (JobConstants.isSeparatedSp(job)) {
            return MapleJob.getJobGrade(job);
        }

        return 0;
    }

    public static int getJobBySkill(int skillId) {
        int result = skillId / 10000;
        if (skillId / 10000 == 8000) {
            result = skillId / 100;
        }
        return result;
    }

    public static boolean isFlipAffectAreaSkill(int skillID) {
        return skillID == 33111013 || skillID == 33121016 || skillID == 33121012 || skillID == 131001207 ||
                skillID == 131001107 || skillID == 4121015 || skillID == 51120057;
    }

    public static boolean isSkillNeedMasterLevel(int skillId) {
        if (is4thNotNeedMasterLevel(skillId)
                || skillId - 46000000 < 1000000 && (skillId % 10000) == 0
                || isSkill9200(skillId)
                || MapleJob.isJob8000(skillId)
                || is初心者Skill(skillId)
                || MapleJob.isJob9500(skillId)) {
            return false;
        } else {
            int jobid = getJobBySkill(skillId);
            int jobTimes = MapleJob.get轉數(jobid);
            return (jobid - 40000 > 5 || jobid - 40000 < 0)
                    && skillId != 42120024 // 影朋‧花狐
                    && !MapleJob.is幻獸師(jobid)
                    && (isNot4thNeedMasterLevel(skillId) || jobTimes == 4 && !MapleJob.is神之子(jobid));
        }
    }

    public static boolean isNot4thNeedMasterLevel(int skillId) {
        if (skillId > 101100101) { // 進階武器投擲
            if (skillId > 101110203) { // 進階旋風落葉斬
                if (skillId == 101120104) { // 進階碎地猛擊
                    return true;
                }
                return skillId == 101120204; // 進階暴風裂擊
            } else {
                // 進階旋風落葉斬 || 進階迴旋之刃 || 進階旋風
                if (skillId == 101110203 || skillId == 101100201 || skillId == 101110102) {
                    return true;
                }
                return skillId == 101110200; // 進階旋風急轉彎
            }
        } else {
            if (skillId == 101100101) { // 進階武器投擲
                return true;
            }
            if (skillId > 4331002) { // 替身術
                // 荊棘特效 || 短刀護佑
                if (skillId == 4340007 || skillId == 4341004) {
                    return true;
                }
                return skillId == 101000101; // 進階威力震擊
            } else {
                // 替身術 || 狂刃風暴 || 翔空落葉斬
                if (skillId == 4331002 || skillId == 4311003 || skillId == 4321006) {
                    return true;
                }
                return skillId == 4330009; // 暗影迴避
            }
        }
    }


    public static boolean is初心者Skill(int skillId) {
        int v1 = getJobBySkill(skillId);
        if (v1 - 40000 > 5 || v1 - 40000 < 0) {
            return MapleJob.is初心者(v1);
        } else {
            return false;
        }
    }

    public static boolean isSkill9200(int skillId) {
        int v1 = 10000 * skillId / 10000;
        return (skillId - 92000000 >= 1000000 || skillId % 10000 > 0)
                && v1 - 92000000 < 1000000
                && v1 % 10000 == 0;
    }

    public static boolean is4thNotNeedMasterLevel(int skillId) {
        if (skillId <= 5320007) { // 雙倍幸運骰子
            if (skillId == 5320007) { // 雙倍幸運骰子
                return true;
            }
            if (skillId > 4210012) { // 貪婪
                if (skillId > 5220012) { // 反擊
                    if (skillId == 5220014) { // 雙倍幸運骰子
                        return true;
                    }
                    return skillId == 5221022; // 海盜砲擊艇
                } else {
                    if (skillId == 5220012) { // 反擊
                        return true;
                    }
                    if (skillId > 4340012) { // 致命的飛毒殺
                        // 反擊姿態 || 雙倍幸運骰子
                        if (skillId < 5120011 || skillId > 5120012) {
                            return false;
                        }
                        return true;
                    }
                    if (skillId == 4340012) { // 致命的飛毒殺
                        return true;
                    }
                    return skillId == 4340010; // 疾速
                }
            } else {
                if (skillId == 4210012) { // 貪婪
                    return true;
                }
                if (skillId > 2221009) { // Null(沒找到技能)
                    // Null(沒找到技能) || 射擊術
                    if (skillId == 2321010 || skillId == 3210015) {
                        return true;
                    }
                    return skillId == 4110012; // 鏢術精通
                } else {
                    // Null(沒找到技能) || 戰鬥精通 || 靈魂復仇
                    if (skillId == 2221009 || skillId == 1120012 || skillId == 1320011) {
                        return true;
                    }
                    return skillId == 2121009; // green card
                }
            }
        }
        if (skillId > 23120011) { // 旋風月光翻轉
            if (skillId > 35120014) { // 雙倍幸運骰子
                if (skillId == 51120000) { // 戰鬥大師
                    return true;
                }
                return skillId == 80001913; // 爆流拳
            } else {
                // 雙倍幸運骰子 || 進階光速雙擊 || 勇士的意志
                if (skillId == 35120014 || skillId == 23120013 || skillId == 23121008) {
                    return true;
                }
                return skillId == 33120010; // 狂暴天性
            }
        }
        if (skillId == 23120011) { // 旋風月光翻轉
            return true;
        }
        if (skillId <= 21120014) { // Null(沒找到技能)
            // Null(沒找到技能) || 雙胞胎猴子 || 楓葉淨化
            if (skillId == 21120014 || skillId == 5321004 || skillId == 5321006) {
                return true;
            }
            return skillId == 21120011; // 快速移動
        }
        if (skillId > 21121008) { // 楓葉淨化
            return skillId == 22171069; // 楓葉淨化
        }
        if (skillId == 21121008) { // 楓葉淨化
            return true;
        }
        if (skillId >= 21120020) { // 動力精通II
            if (skillId > 21120021) { // 終極研究 II
                return false;
            }
            return true;
        }
        return false;
    }


    public static int get紫扇傳授UnknownValue(int skillId) {
        int result;
        if (skillId == 40020002 || skillId == 80000004) {
            result = 100;
        } else {
            result = 0;
        }
        return result;
    }


    public static int getAvailableVCoreSpace(int level) {
        if (level >= 200) {
            return 4 + (level - 250) / 5;
        } else {
            return 4;
        }
    }

    public static boolean isStanceSkill(int skillID) {
        if (skillID > 22170072) {
            if (skillID > 65111004) {
                return skillID == 80001415 || skillID == 80001845;
            } else {
                return skillID == 65111004 || skillID == 23121054 || skillID == 32111014;
            }
        }
        if (skillID == 22170072)
            return true;
        if (skillID <= 5321010) {
            return skillID == 5321010 || skillID == 2121004 || skillID == 2221004 || skillID == 2321004;
        }
        return skillID >= 20040219 && (skillID <= 20040220 || skillID == 21121003);
    }

    public static boolean isExplosionSkill(int nSkillID) {
        if (nSkillID > 13121009) {
            return nSkillID == 36110005 || nSkillID == 65101006;
        } else {
            return nSkillID == 13121009 || nSkillID == 11121013 || nSkillID == 12100029;
        }
    }

    public static boolean isKeyDownSkill(int skillId) {
        return skillId == 2321001 || skillId == 80001836 || skillId == 37121052 || skillId == 36121000 ||
                skillId == 37121003 || skillId == 36101001 || skillId == 33121114 || skillId == 33121214 ||
                skillId == 35121015 || skillId == 33121009 || skillId == 32121003 || skillId == 31211001 ||
                skillId == 31111005 || skillId == 30021238 || skillId == 31001000 || skillId == 31101000 ||
                skillId == 80001887 || skillId == 80001836 || skillId == 80001880 || skillId == 80001629 ||
                skillId == 60011216 || skillId == 65121003 || skillId == 80001587 || skillId == 131001008 ||
                skillId == 142111010 || skillId == 131001004 || skillId == 95001001 || skillId == 101110100 ||
                skillId == 101110101 || skillId == 101110102 || skillId == 27111100 || skillId == 12121054 ||
                skillId == 11121052 || skillId == 11121055 || skillId == 5311002 || skillId == 4341002 ||
                skillId == 5221004 || skillId == 5221022 || skillId == 3121020 || skillId == 3101008 ||
                skillId == 3111013 || skillId == 2321001 || skillId == 1311011 || skillId == 2221011 ||
                skillId == 2221052 || skillId == 22171083 || skillId == 25111005 || skillId == 25121030 ||
                skillId == 27101202 || skillId == 25111005 || skillId == 23121000 || skillId == 22171083 ||
                skillId == 14121004 || skillId == 13111020 || skillId == 13121001 || skillId == 14111006 ||
                skillId == 20041226 || (skillId >= 80001389 && skillId <= 80001392);
    }

    public static boolean isSuperNovaSkill(int nSkillID) {
        return nSkillID == 4221052 || nSkillID == 65121052;
    }

    public static boolean isRwMultiChargeSkill(int nSkillID) {
        boolean v1 = true;
        if (nSkillID > 37110001) {
            if (nSkillID == 37110004 || nSkillID == 37111000) {
                return true;
            }
            v1 = nSkillID == 37111003;
        } else {
            if (nSkillID == 37110001) {
                return true;
            }
            if (nSkillID > 37100002) {
                v1 = nSkillID == 37101001;
            } else {
                if (nSkillID == 37100002 || nSkillID == 37000010) {
                    return true;
                }
                v1 = nSkillID == 37001001;
            }
        }
        return v1;
    }

    public static boolean isUnregisterdSkill(int nSkillID) {
        boolean result;
        int v1;
        v1 = nSkillID / 10000;
        if (nSkillID / 10000 == 8000) {
            v1 = nSkillID / 100;
        }
        if (nSkillID > 0 && v1 == 9500) {
            result = false;
        } else {
            result = nSkillID / 10000000 == 9;
        }
        return result;
    }

    public static boolean isMatchSkill(boolean bIsBeta, int nSkillID) {
        int v2 = nSkillID / 10000;
        int v3 = nSkillID / 10000;
        boolean result;
        if (nSkillID / 10000 == 8000) {
            v3 = nSkillID / 100;
        }
        if (v2 == 8000) {
            v2 = nSkillID / 100;
        }
        if (MapleJob.isBeginner(v3) || (nSkillID > 0 && v2 == 9500)) {
            result = true;
        } else {
            result = isZeroAlphaSkill(nSkillID) && !bIsBeta || isZeroBetaSkill(nSkillID) && bIsBeta;
        }
        return result;
    }

    public static boolean isZeroAlphaSkill(int nSkillID) {
        boolean result;
        int v2 = nSkillID / 10000;
        if (nSkillID / 10000 == 8000) {
            v2 = nSkillID / 100;
        }
        if (isZeroSkill(nSkillID) || MapleJob.isBeginner(v2)) {
            result = false;
        } else {
            result = nSkillID % 1000 / 100 == 2;
        }
        return result;
    }

    public static boolean isZeroBetaSkill(int nSkillID) {
        boolean result;
        int v2 = nSkillID / 10000;
        if (nSkillID / 10000 == 8000) {
            v2 = nSkillID / 100;
        }
        if (isZeroSkill(nSkillID) || MapleJob.isBeginner(v2)) {
            result = false;
        } else {
            result = nSkillID % 1000 / 100 == 1;
        }
        return result;
    }

    public static boolean isZeroSkill(int nSkillID) {
        int v1 = nSkillID / 10000;
        boolean v2;
        if (nSkillID / 10000 == 8000) {
            v1 = nSkillID / 100;
        }
        if (v1 == 10000 || v1 == 10100 || v1 == 10110 || v1 == 10111 || v1 == 10112) {
            v2 = true;
        } else {
            v2 = false;
        }
        return v2;
    }

    public static boolean isRushBombSkill(int skillID) {
        boolean v1;
        if (skillID > 40021186) {
            if (skillID > 80002300) {
                if (skillID > 400001018) {
                    return (skillID - 400031003) <= 1;
                }
                if (skillID == 400001018 || skillID == 80011380)
                    return true;
                v1 = skillID == 80011386;
            } else {
                if (skillID == 80002300)
                    return true;
                if (skillID > 61111113) {
                    if (skillID == 61111218)
                        return true;
                    v1 = skillID == 80002247;
                } else {
                    if (skillID == 61111113 || skillID == 42120003)
                        return true;
                    v1 = skillID == 61111100;
                }
            }
        } else {
            if (skillID == 40021186)
                return true;
            if (skillID > 14111022) {
                if (skillID > 27121201) {
                    v1 = skillID == 31201001;
                } else {
                    if (skillID == 27121201
                            || skillID == 22140015) {
                        return true;
                    }
                    v1 = skillID - (22140015) == 9;
                }
            } else {
                if (skillID == 14111022)
                    return true;
                if (skillID > 5101014) {
                    if (skillID == 5301001)
                        return true;
                    v1 = skillID == 12121001;
                } else {
                    if (skillID == 5101014 || skillID == 2221012)
                        return true;
                    v1 = skillID == 5101012;
                }
            }
        }
        return v1;
    }

    public static boolean isUsercloneSummonedAbleSkill(int skillID) {
        return skillID == 14111020 || skillID == 14101019 || (skillID >= 14101019 && skillID <= 14101021) ||
                skillID == 14120045 || (skillID >= 14121000 && skillID == 14121002);
    }

    public static boolean isAranFallingStopSkill(int skillID) {
        switch (skillID) {
            case 21110028:
            case 21120025:
            case 21110026:
            case 21001010:
            case 21000006:
            case 21000007:
            case 21110022:
            case 21110023:
            case 80001925:
            case 80001926:
            case 80001927:
            case 80001936:
            case 80001937:
            case 80001938:
                return true;
            default:
                return false;
        }
    }

    public static boolean isScreenCenterAttackSkill(int skillID) {
        return skillID == 80001431 || skillID == 100001283 || skillID == 21121057 || skillID == 13121052 ||
                skillID == 14121052 || skillID == 15121052;
    }

    public static int getAdvancedCountHyperSkill(int skillId) {
        switch (skillId) {
            case 4121013:
                return 4120051;
            case 5321012:
                return 5320051;
            default:
                return 0;
        }
    }

    public static int getAdvancedAttackCountHyperSkill(int skillId) {
        switch (skillId) {
            case 25121005:
                return 25120148;
            case 31121001:
                return 31120050;
            case 31111005:
                return 31120044;
            case 22140023:
                return 22170086;
            case 21120022:
            case 21121015:
            case 21121016:
            case 21121017:
                return 21120066;
            case 21120006:
                return 21120049;
            case 21110020:
            case 21111021:
                return 21120047;
            case 15121002:
                return 15120048;
            case 14121002:
                return 14120045;
            case 15111022:
            case 15120003:
                return 15120045;
            case 51121008:
                return 51120048;
            case 32111003:
                return 32120047;
            case 35121016:
                return 35120051;
            case 37110002:
                return 37120045;
            case 51120057:
                return 51120058;
            case 51121007:
                return 51120051;
            case 65121007:
            case 65121008:
            case 65121101:
                return 65120051;
            case 61121201:
            case 61121100:
                return 61120045;
            case 51121009:
                return 51120058;
            case 13121002:
                return 13120048;
            case 5121016:
            case 5121017:
                return 5120051;
            case 3121015:
                return 3120048;
            case 2121006:
                return 2120048;
            case 2221006:
                return 2220048;
            case 1221011:
                return 1220050;
            case 1120017:
            case 1121008:
                return 1120051;
            case 1221009:
                return 1220048;
            case 4331000:
                return 4340045;
            case 3121020:
                return 3120051;
            case 3221017:
                return 3220048;
            case 4221007:
                return 4220048;
            case 4341009:
                return 4340048;
            case 5121007:
                return 5120048;
            case 5321004:
                return 5320043;
            // if ( nSkillID != &loc_A9B1CF ) nothing done with line 172?
            case 12110028:
            case 12000026:
            case 12100028:
                return 12120045;
            case 12120010:
                return 12120045;
            case 12120011:
                return 12120046;
            default:
                return 0;
        }
    }

    public static boolean isShikigamiHauntingSkill(int skillID) {
        switch (skillID) {
            case 80001850:
            case 42001000:
            case 42001005:
            case 42001006:
            case 40021185:
            case 80011067:
                return true;
            default:
                return false;
        }
    }

    public static boolean isKinesisPsychicLockSkill(int nSkillID) {
        if (nSkillID > 142111002) {
            if (nSkillID < 142120000 || nSkillID > 142120002 && nSkillID != 142120014)
                return false;
        } else if (nSkillID != 142111002 && nSkillID != 142100010 && nSkillID != 142110003 && nSkillID != 142110015) {
            return false;
        }
        return true;
    }

    public static boolean isKeydownSkillRectMoveXY(int skillId) {
        return skillId == 13111020 || skillId == 112111016; // 寒冰亂舞 || 旋風飛行
    }

    public static int getLinkSkillLevelByCharLevel(int level) {
        int res = 0;
        if (level >= LINK_SKILL_3_LEVEL) {
            res = 3;
        } else if (level >= LINK_SKILL_2_LEVEL) {
            res = 2;
        } else if (level >= LINK_SKILL_1_LEVEL) {
            res = 1;
        }
        return res;
    }

    public static int getLinkSkillByJob(short job) {
        if (MapleJob.is重砲指揮官(job)) { // Pirate Blessing
            return 80000000;
        } else if (MapleJob.is皇家騎士團(job)) { // Cygnus Blessing
            return 80000070;
        } else if (MapleJob.is精靈遊俠(job)) { // Elven Blessing
            return 80001040;
        } else if (MapleJob.is惡魔殺手(job)) { // Fury Unleashed
            return 80000001;
        } else if (MapleJob.is惡魔復仇者(job)) { // Wild Rage
            return 80000050;
        } else if (MapleJob.is蒼龍俠客(job)) { // Core Aura
            return 80001151;
        } else if (MapleJob.is幻影俠盜(job)) { // Phantom Instinct
            return 80000002;
        } else if (MapleJob.is米哈逸(job)) { // Knight's Watch
            return 80001140;
        } else if (MapleJob.is夜光(job)) { // Light Wash
            return 80000005;
        } else if (MapleJob.is天使破壞者(job)) { // Terms and Conditions
            return 80001155;
        } else if (MapleJob.is劍豪(job)) { // Keen Edge
            return 80000003;
        } else if (MapleJob.is陰陽師(job)) { // Elementalism
            return 80000004;
        } else if (MapleJob.is凱撒(job)) { // Iron Will
            return 80000006;
        } else if (MapleJob.is傑諾(job)) { // Hybrid Logic
            return 80000047;
        } else if (MapleJob.is幻獸師(job)) { // Focus Spirit
            return 80010006;
        }
        return 0;
    }

    public static boolean isSetPoseSkill(int skillID) {
        return skillID == 11101022 || skillID == 11111022;
    }

    public static boolean isMechanicMetalarmorSkill(int skillID) {
        return skillID == 35001002 || skillID == 35111003;
    }

    public static boolean isAntiRepeatBuffSkill(int skillID)
    {
        boolean v1; // zf

        if ( skillID > 15001022 )
        {
            if ( skillID > 40011186 )
            {
                if ( skillID > 80000365 )
                {
                    if ( skillID > 131001018 )
                    {
                        if ( skillID > 400021035 )
                        {
                            if ( skillID > 400051001 )
                            {
                                v1 = skillID == 400051015;
                            }
                            else
                            {
                                if ( skillID == 400051001 || skillID == 400031002 )
                                    return true;
                                v1 = skillID == 400041008;
                            }
                        }
                        else
                        {
                            if ( skillID == 400021035 )
                                return true;
                            if ( skillID > 400011010 )
                            {
                                v1 = skillID == 400021024;
                            }
                            else
                            {
                                if ( skillID == 400011010 || skillID == 142121016 )
                                    return true;
                                v1 = skillID == 400001020;
                            }
                        }
                    }
                    else
                    {
                        if ( skillID == 131001018 )
                            return true;
                        if ( skillID > 100001271 )
                        {
                            if ( skillID > 112121010 )
                            {
                                v1 = skillID == 112121056;
                            }
                            else
                            {
                                if ( skillID == 112121010 || skillID == 110001511 )
                                    return true;
                                v1 = skillID == 112121006;
                            }
                        }
                        else
                        {
                            if ( skillID == 100001271 )
                                return true;
                            if ( skillID > 80010019 )
                            {
                                if ( skillID == 80011032 )
                                    return true;
                                v1 = skillID == 100001268;
                            }
                            else
                            {
                                if ( skillID == 80010019 || skillID == 80001361 )
                                    return true;
                                v1 = skillID == 80001816;
                            }
                        }
                    }
                    return v1;
                }
                if ( skillID == 80000365 )
                    return true;
                if ( skillID > 61101002 )
                {
                    if ( skillID > 65111100 )
                    {
                        switch ( skillID )
                        {
                            case 65121004:
                            case 65121009:
                            case 65121011:
                            case 65121053:
                            case 65121054:
                                return true;
                            default:
                                return false;
                        }
                    }
                    if ( skillID == 65111100 )
                        return true;
                    if ( skillID > 61120007 )
                    {
                        if ( skillID == 61121014 )
                            return true;
                        v1 = skillID == 61121217;
                        return v1;
                    }
                    if ( skillID == 61120007 )
                        return true;
                    if ( skillID >= 61110211 )
                    {
                        if ( skillID <= 61110212 )
                            return true;
                        v1 = skillID == 61111008;
                        return v1;
                    }
                }
                else
                {
                    if ( skillID == 61101002 )
                        return true;
                    if ( skillID > 42121054 )
                    {
                        if ( skillID > 51121005 )
                        {
                            if ( skillID >= 51121053 )
                            {
                                return skillID <= 51121054;
                            }
                        }
                        else
                        {
                            if ( skillID == 51121005 || skillID == 51101004 )
                                return true;
                            if ( skillID > 51111003 )
                            {
                                return skillID <= 51111005;
                            }
                        }
                    }
                    else
                    {
                        if ( skillID >= 42121053 )
                            return true;
                        if ( skillID <= 41121005 )
                        {
                            if ( skillID == 41121005 || skillID == 41001010 )
                                return true;
                            v1 = skillID == 41101003;
                            return v1;
                        }
                        if ( skillID >= 41121053 )
                        {
                            if ( skillID <= 41121054 )
                                return true;
                            v1 = skillID == 42121006;
                            return v1;
                        }
                    }
                }
            }
            else
            {
                if ( skillID == 40011186 )
                    return true;
                if ( skillID > 27121006 )
                {
                    if ( skillID > 32121053 )
                    {
                        if ( skillID > 35121053 )
                        {
                            if ( skillID > 36121054 )
                            {
                                v1 = skillID == 37121053;
                            }
                            else
                            {
                                if ( skillID >= 36121053 || skillID == 36111004 )
                                    return true;
                                v1 = skillID == 36121008;
                            }
                        }
                        else
                        {
                            if ( skillID == 35121053 )
                                return true;
                            if ( skillID > 35111013 )
                            {
                                v1 = skillID == 35120014;
                            }
                            else
                            {
                                if ( skillID == 35111013 || skillID == 33121007 )
                                    return true;
                                v1 = skillID == 33121053;
                            }
                        }
                        return v1;
                    }
                    if ( skillID == 32121053 )
                        return true;
                    if ( skillID > 31211004 )
                    {
                        if ( skillID > 31221054 )
                        {
                            v1 = skillID == 32121007;
                        }
                        else
                        {
                            if ( skillID >= 31221053 || skillID == 31221001 )
                                return true;
                            v1 = skillID == 31221008;
                        }
                        return v1;
                    }
                    if ( skillID >= 31211003 )
                        return true;
                    if ( skillID > 31011001 )
                    {
                        if ( skillID >= 31121003 )
                        {
                            if ( skillID <= 31121005 )
                                return true;
                            v1 = skillID == 31121053;
                            return v1;
                        }
                    }
                    else
                    {
                        if ( skillID == 31011001 || skillID == 27121009 )
                            return true;
                        if ( skillID > 27121052 )
                        {
                            if ( skillID > 27121054 )
                                return false;
                            return true;
                        }
                    }
                }
                else
                {
                    if ( skillID == 27121006 )
                        return true;
                    if ( skillID > 23111004 )
                    {
                        if ( skillID > 24121053 )
                        {
                            if ( skillID <= 25121132 )
                            {
                                if ( skillID == 25121132 || skillID == 25101009 )
                                    return true;
                                v1 = skillID == 25121108;
                                return v1;
                            }
                            if ( skillID >= 27111005 )
                            {
                                return skillID <= 27111006;
                            }
                        }
                        else
                        {
                            if ( skillID == 24121053 )
                                return true;
                            if ( skillID <= 24111003 )
                            {
                                if ( skillID != 24111003 )
                                {
                                    switch ( skillID )
                                    {
                                        case 23121004:
                                        case 23121005:
                                        case 23121053:
                                        case 23121054:
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                                return true;
                            }
                            if ( skillID == 24121004 )
                                return true;
                            if ( skillID > 24121006 )
                            {
                                return skillID <= 24121008;
                            }
                        }
                    }
                    else
                    {
                        if ( skillID == 23111004 )
                            return true;
                        if ( skillID > 21121000 )
                        {
                            if ( skillID > 22171073 )
                            {
                                v1 = skillID == 22171082;
                                return v1;
                            }
                            if ( skillID == 22171073 )
                                return true;
                            if ( skillID >= 21121053 )
                            {
                                if ( skillID <= 21121054 )
                                    return true;
                                v1 = skillID == 22171068;
                                return v1;
                            }
                        }
                        else
                        {
                            if ( skillID == 21121000 )
                                return true;
                            if ( skillID <= 15121053 )
                            {
                                if ( skillID == 15121053 || skillID == 15121000 )
                                    return true;
                                v1 = skillID == 15121005;
                                return v1;
                            }
                            if ( skillID >= 20031209 )
                            {
                                if ( skillID <= 20031210 )
                                    return true;
                                v1 = skillID == 21111012;
                                return v1;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            if ( skillID == 15001022 )
                return true;
            if ( skillID > 4341000 )
            {
                if ( skillID > 5721054 )
                {
                    if ( skillID > 13101024 )
                    {
                        if ( skillID > 14001007 )
                        {
                            if ( skillID > 14121000 )
                            {
                                v1 = skillID == 14121053;
                                return v1;
                            }
                            if ( skillID == 14121000 )
                                return true;
                            if ( skillID >= 14001022 )
                            {
                                if ( skillID <= 14001023 )
                                    return true;
                                v1 = skillID == 14001027;
                                return v1;
                            }
                        }
                        else
                        {
                            if ( skillID == 14001007 )
                                return true;
                            if ( skillID > 13121053 )
                            {
                                v1 = skillID == 14001003;
                                return v1;
                            }
                            if ( skillID == 13121053 || skillID == 13121000 )
                                return true;
                            if ( skillID > 13121003 )
                            {
                                if ( skillID > 13121005 )
                                    return false;
                                return true;
                            }
                        }
                    }
                    else
                    {
                        if ( skillID == 13101024 )
                            return true;
                        if ( skillID <= 11121054 )
                        {
                            if ( skillID >= 11121053 )
                                return true;
                            if ( skillID > 11111023 )
                            {
                                if ( skillID == 11121000 )
                                    return true;
                                v1 = skillID == 11121005;
                            }
                            else
                            {
                                if ( skillID >= 11111022 || skillID == 11001022 )
                                    return true;
                                v1 = skillID == 11101022;
                            }
                            return v1;
                        }
                        if ( skillID > 12121053 )
                        {
                            v1 = skillID == 13001022;
                            return v1;
                        }
                        if ( skillID == 12121053 )
                            return true;
                        if ( skillID >= 12101000 )
                        {
                            if ( skillID <= 12101001 )
                                return true;
                            v1 = skillID == 12121000;
                            return v1;
                        }
                    }
                }
                else
                {
                    if ( skillID >= 5721053 )
                        return true;
                    if ( skillID > 5221018 )
                    {
                        if ( skillID > 5321053 )
                        {
                            if ( skillID > 5711024 )
                            {
                                v1 = skillID == 5721000;
                            }
                            else
                            {
                                if ( skillID == 5711024 || skillID == 5701013 )
                                    return true;
                                v1 = skillID == 5711007;
                            }
                            return v1;
                        }
                        if ( skillID == 5321053 )
                            return true;
                        if ( skillID > 5311005 )
                        {
                            if ( skillID >= 5320007 )
                            {
                                if ( skillID <= 5320008 )
                                    return true;
                                v1 = skillID == 5321005;
                                return v1;
                            }
                        }
                        else
                        {
                            if ( skillID >= 5311004 )
                                return true;
                            if ( skillID >= 5221053 )
                            {
                                if ( skillID <= 5221054 )
                                    return true;
                                v1 = skillID == 5301003;
                                return v1;
                            }
                        }
                    }
                    else
                    {
                        if ( skillID == 5221018 )
                            return true;
                        if ( skillID <= 5121015 )
                        {
                            if ( skillID == 5121015 )
                                return true;
                            if ( skillID > 5120012 )
                            {
                                if ( skillID == 5121000 )
                                    return true;
                                v1 = skillID == 5121009;
                            }
                            else
                            {
                                if ( skillID == 5120012 || skillID == 4341053 )
                                    return true;
                                v1 = skillID == 5111007;
                            }
                            return v1;
                        }
                        if ( skillID > 5220014 )
                        {
                            v1 = skillID == 5221000;
                            return v1;
                        }
                        if ( skillID == 5220014 )
                            return true;
                        if ( skillID >= 5121053 )
                        {
                            if ( skillID <= 5121054 )
                                return true;
                            v1 = skillID == 5211007;
                            return v1;
                        }
                    }
                }
            }
            else
            {
                if ( skillID == 4341000 )
                    return true;
                if ( skillID > 2221053 )
                {
                    if ( skillID > 3211012 )
                    {
                        if ( skillID > 4121000 )
                        {
                            if ( skillID > 4221053 )
                            {
                                v1 = skillID == 4301003;
                            }
                            else
                            {
                                if ( skillID == 4221053 || skillID == 4121053 )
                                    return true;
                                v1 = skillID == 4221000;
                            }
                        }
                        else
                        {
                            if ( skillID == 4121000 )
                                return true;
                            if ( skillID > 4001003 )
                            {
                                if ( skillID == 4001005 )
                                    return true;
                                v1 = skillID == 4101011;
                            }
                            else
                            {
                                if ( skillID == 4001003 || skillID == 3221000 )
                                    return true;
                                v1 = skillID == 3221053;
                            }
                        }
                    }
                    else
                    {
                        if ( skillID == 3211012 )
                            return true;
                        if ( skillID > 2321053 )
                        {
                            if ( skillID > 3121002 )
                            {
                                v1 = skillID == 3121053;
                            }
                            else
                            {
                                if ( skillID == 3121002 || skillID == 3111011 )
                                    return true;
                                v1 = skillID == 3121000;
                            }
                        }
                        else
                        {
                            if ( skillID == 2321053 )
                                return true;
                            if ( skillID > 2311003 )
                            {
                                if ( skillID == 2321000 )
                                    return true;
                                v1 = skillID == 2321005;
                            }
                            else
                            {
                                if ( skillID == 2311003 || skillID == 2301004 )
                                    return true;
                                v1 = skillID == 2311001;
                            }
                        }
                    }
                    return v1;
                }
                if ( skillID == 2221053 )
                    return true;
                if ( skillID > 1301007 )
                {
                    if ( skillID <= 2101010 )
                    {
                        if ( skillID == 2101010 )
                            return true;
                        if ( skillID > 1321015 )
                        {
                            if ( skillID == 1321053 )
                                return true;
                            v1 = skillID == 2101001;
                        }
                        else
                        {
                            if ( skillID >= 1321014 || skillID == 1311015 )
                                return true;
                            v1 = skillID == 1321000;
                        }
                        return v1;
                    }
                    if ( skillID > 2201001 )
                    {
                        v1 = skillID == 2221000;
                        return v1;
                    }
                    if ( skillID == 2201001 || skillID == 2121000 )
                        return true;
                    if ( skillID > 2121052 )
                    {
                        if ( skillID > 2121054 )
                            return false;
                        return true;
                    }
                }
                else
                {
                    if ( skillID >= 1301006 )
                        return true;
                    if ( skillID <= 1211010 )
                    {
                        if ( skillID == 1211010 )
                            return true;
                        if ( skillID > 1121000 )
                        {
                            if ( skillID != 1121016 && (skillID <= 1121052 || skillID > 1121054) )
                                return false;
                            return true;
                        }
                        if ( skillID == 1121000 || skillID == 1000003 )
                            return true;
                        v1 = skillID == 1101006;
                        return v1;
                    }
                    if ( skillID <= 1221014 )
                    {
                        if ( skillID == 1221014 || skillID == 1211013 )
                            return true;
                        v1 = skillID == 1221000;
                        return v1;
                    }
                    if ( skillID >= 1221052 )
                    {
                        if ( skillID > 1221053 )
                            return false;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean is混沌共鳴(int skillID) {
        return skillID == 400021041 || skillID > 400021048 && skillID <= 400021050;
    }

    public static int getOriginalOfLinkedSkill(int skillID) {
        int result = 0;
        switch (skillID) {
            case 80001040: // 精靈的祝福
                result = 20021110;
                break;
            case 80001140: // 光之守護 [連結技能] 獲得聖殿騎士 米哈逸的守護，一定時間內受到敵人的攻擊也不會被擊退。
                result = 50001214;
                break;
            case 80001155: // 靈魂契約
                result = 60011219;
                break;
            case 80000378:
                result = 30000077;
                break;
            case 80000334:
                result = 30000075;
                break;
            case 80000335:
                result = 30000076;
                break;
            case 80000369:
                result = 20010294;
                break;
            case 80000370:
                result = 20000297;
                break;
            case 80000333:
                result = 30000074;
                break;
            case 80000000:
                result = 110;
                break;
            case 80000001:
                result = 30010112;
                break;
            case 80000002:
                result = 20030204;
                break;
            case 80000005:
                result = 20040218;
                break;
            case 80000006:
                result = 60000222;
                break;
            case 80000047:
                result = 30020233;
                break;
            case 80000050:
                result = 30010241;
                break;
            case 80000066:
                result = 10000255;
                break;
            case 80000067:
                result = 10000256;
                break;
            case 80000068:
                result = 10000257;
                break;
            case 80000069:
                result = 10000258;
                break;
            case 80000070:
                result = 10000259;
                break;
            case 80000110:
                result = 100000271;
                break;
            case 80000169:
                result = 20050286;
                break;
            case 80000188:
                result = 140000292;
                break;
            case 80000004:
                result = 40020002;
                break;
            case 0:
                result = 0;
                break;
            default:
                LoggerFactory.getLogger(SkillConstants.class).error("Unknown corresponding link skill for link skill id " + skillID);
        }
        return result;
    }

    boolean isRpThreeCutSkill(int skillID) {
        boolean v1;
        if (skillID > 41111000) {
            if (skillID > 41121012) {
                switch (skillID) {
                    case 80011036:
                    case 80011037:
                    case 80011038:
                    case 80011070:
                    case 80011071:
                    case 80011072:
                        return true;
                    default:
                        return false;
                }
            }
            if (skillID >= 41121011)
                return true;
            if (skillID < 41111011)
                return false;
            if (skillID <= 41111012)
                return true;
            v1 = skillID == 41121000;
            return v1;
        }
        if (skillID == 41111000)
            return true;
        if (skillID > 41001005) {
            return !(skillID != 41101000 && (skillID <= 41101007 || skillID > 41101009));
        }
        if (skillID < 41001004) {
            if (skillID < 40011183)
                return false;
            if (skillID > 40011185) {
                v1 = skillID == 41001000;
                return v1;
            }
        }
        return true;
    }

    public boolean isBmageSummonedDeathSkill(int skillID) {
        boolean v1; // zf
        if (skillID > 32110017) {
            v1 = skillID == 32120019;
        } else {
            if (skillID == 32110017 || skillID == 32001014)
                return true;
            v1 = skillID == 32100010;
        }
        return v1;
    }

    public boolean isBmageAuraSkill(int nSkillID) {
        if (nSkillID > 32111012) {
            if (nSkillID < 32121017 || nSkillID > 32121018 && nSkillID != 400021006)
                return false;
        } else if (nSkillID != 32111012 && nSkillID != 32001016 && nSkillID != 32101009) {
            return false;
        }
        return true;
    }

    public boolean isBackStepShotSkill(int nSkillID) {
        boolean v1; // zf
        if (nSkillID > 5311003) {
            if (nSkillID == 36101009)
                return true;
            v1 = nSkillID == 112111002;
        } else {
            if (nSkillID == 5311003 || nSkillID == 3101008 || nSkillID == 5011002)
                return true;
            v1 = nSkillID == 5201006;
        }
        return v1;
    }

    public boolean isKinesisPsychicforceSKill(int nSkillID) {
        if (nSkillID > 14499728) {
            if (nSkillID == 80011045) {
                return true;
            }
        } else if (nSkillID == 14499728
                || nSkillID == 41001002 || nSkillID > 41001005 && nSkillID <= 41001008) {
            return true;
        }
        return false;
    }

    public static boolean isHiddenIgnoreRequestTimeSkill(int nSkillID) {
        if (nSkillID > 25120110) {
            if (nSkillID == 25120115)
                return true;
        } else if (nSkillID == 25120110 || nSkillID >= 25100009 && nSkillID <= 25100010) {
            return true;
        }
        if (isCnnCancelSkill(nSkillID)) {
            return true;
        }
        return nSkillID == 25100002;
    }

    public static boolean isCnnCancelSkill(int nSkillID) {
        boolean v1;
        if (nSkillID <= 51111012 && (nSkillID >= 51111011 || (nSkillID - 51001006) <= 7))
            return true;
        if (nSkillID > 23121015) {
            if (nSkillID > 80001881) {
                if (nSkillID == 142111010 || nSkillID == 400011029)
                    return true;
                v1 = nSkillID == 400041007;
            } else {
                if (nSkillID == 80001881 || nSkillID == 25101205 || nSkillID == 37100002)
                    return true;
                v1 = nSkillID == 37110004;
            }
        } else {
            if (nSkillID >= 23121014)
                return true;
            if (nSkillID > 21110018) {
                if (nSkillID == 22001011 || nSkillID == 22141017)
                    return true;
                v1 = nSkillID == 22170070;
            } else {
                if (nSkillID == 21110018)
                    return true;
                if (nSkillID > 14001024) {
                    v1 = nSkillID == 20051284;
                } else {
                    if (nSkillID == 14001024 || nSkillID == 4331006)
                        return true;
                    v1 = nSkillID == 11121014;
                }
            }
        }
        return v1;
    }

    public static int getLinkedSkill(int skillId) {
        switch (skillId) {
            case 1111014: // 虎咆哮_下
                return 1111008; // 虎咆哮
            case 21000006:
            case 21000007:
                return 21001010; // 粉碎震撼
            default:
                return 0;
        }
    }

}
