package com.msemu.world.constants;

/**
 * Created by Weber on 2018/3/31.
 */
public class SkillConstants {
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
}
