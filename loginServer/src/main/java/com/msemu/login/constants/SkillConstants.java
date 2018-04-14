package com.msemu.login.constants;

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

}
