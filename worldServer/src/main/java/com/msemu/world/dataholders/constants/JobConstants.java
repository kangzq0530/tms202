package com.msemu.world.dataholders.constants;

import com.msemu.world.model.player.enums.MapleJob;

/**
 * Created by Weber on 2018/3/21.
 */
public class JobConstants {
    public static boolean is冒險家(final int job) {
        return job / 1000 == 0;
    }

    public static boolean is英雄(final int job) {
        return job / 10 == 11;
    }

    public static boolean is聖騎士(final int job) {
        return job / 10 == 12;
    }

    public static boolean is黑騎士(final int job) {
        return job / 10 == 13;
    }

    public static boolean is大魔導士_火毒(final int job) {
        return job / 10 == 21;
    }

    public static boolean is大魔導士_冰雷(final int job) {
        return job / 10 == 22;
    }

    public static boolean is主教(final int job) {
        return job / 10 == 23;
    }

    public static boolean is箭神(final int job) {
        return job / 10 == 31;
    }

    public static boolean is神射手(final int job) {
        return job / 10 == 32;
    }

    public static boolean is夜使者(final int job) {
        return job / 10 == 41;
    }

    public static boolean is暗影神偷(final int job) {
        return job / 10 == 42;
    }

    public static boolean is影武者(final int job) {
        return job / 10 == 43; // sub == 1 && job == 400
    }

    public static boolean is拳霸(final int job) {
        return job / 10 == 51;
    }

    public static boolean is槍神(final int job) {
        return job / 10 == 52;
    }

    public static boolean is重砲指揮官(final int job) {
        return job / 10 == 53 || job == 501 || job == 1;
    }

    public static boolean is蒼龍俠客(final int job) {
        return job / 10 == 57 || job == 508;
    }

    public static boolean is管理員(final int job) {
        return job == 800 || job == 900 || job == 910;
    }

    public static boolean is皇家騎士團(final int job) {
        return job / 1000 == 1;
    }

    public static boolean is聖魂劍士(final int job) {
        return job / 100 == 11;
    }

    public static boolean is烈焰巫師(final int job) {
        return job / 100 == 12;
    }

    public static boolean is破風使者(final int job) {
        return job / 100 == 13;
    }

    public static boolean is暗夜行者(final int job) {
        return job / 100 == 14;
    }

    public static boolean is閃雷悍將(final int job) {
        return job / 100 == 15;
    }

    public static boolean is英雄團(final int job) {
        return job / 1000 == 2;
    }

    public static boolean is狂狼勇士(final int job) {
        return job / 100 == 21 || job == 2000;
    }

    public static boolean is龍魔導士(final int job) {
        return job / 100 == 22 || job == 2001;
    }

    public static boolean is精靈遊俠(final int job) {
        return job / 100 == 23 || job == 2002;
    }

    public static boolean is幻影俠盜(final int job) {
        return job / 100 == 24 || job == 2003;
    }

    public static boolean is夜光(final int job) {
        return job / 100 == 27 || job == 2004;
    }

    public static boolean is隱月(int job) {
        return job / 100 == 25 || job == 2005;
    }

    public static boolean is末日反抗軍(final int job) {
        return job / 1000 == 3;
    }

    public static boolean is惡魔(final int job) {
        return is惡魔殺手(job) || is惡魔復仇者(job) || job == 3001;
    }

    public static boolean is惡魔殺手(final int job) {
        return job / 10 == 311 || job == 3100;
    }

    public static boolean is惡魔復仇者(int job) {
        return job / 10 == 312 || job == 3101;
    }

    public static boolean is煉獄巫師(final int job) {
        return job / 100 == 32;
    }

    public static boolean is狂豹獵人(final int job) {
        return job / 100 == 33;
    }

    public static boolean is機甲戰神(final int job) {
        return job / 100 == 35;
    }

    public static boolean is傑諾(final int job) {
        return job / 100 == 36 || job == 3002;
    }

    public static boolean is爆拳槍神(final int job) {
        return job / 100 == 37;
    }

    public static boolean is曉の陣(int job) {
        return job / 1000 == 4;
    }

    public static boolean is劍豪(int job) {
        return job / 100 == 41 || job == 4001;
    }

    public static boolean is陰陽師(int job) {
        return job / 100 == 42 || job == 4002;
    }

    public static boolean is騎士團團長(final int job) {
        return job / 1000 == 5;
    }

    public static boolean is米哈逸(final int job) {
        return job / 100 == 51 || job == 5000;
    }

    public static boolean is超新星(final int job) {
        return job / 1000 == 6;
    }

    public static boolean is凱撒(final int job) {
        return job / 100 == 61 || job == 6000;
    }

    public static boolean is天使破壞者(final int job) {
        return job / 100 == 65 || job == 6001;
    }

    public static boolean is神之子(int job) {
        return job == 10000 || job == 10100 || job == 10110 || job == 10111 || job == 10112;
    }

    public static boolean is幻獸師(final int job) {
        return job / 100 == 112 || job == 11000;
    }

    public static boolean is皮卡啾(final int job) {
        return job / 100 == 131 || job == 13000;
    }

    public static boolean is凱內西斯(final int job) {
        return job / 100 == 142 || job == 14000;
    }

    public static boolean is劍士(final int job) {
        return getJobBranch(job) == 1;
    }

    public static boolean is法師(final int job) {
        return getJobBranch(job) == 2;
    }

    public static boolean is弓箭手(final int job) {
        return getJobBranch(job) == 3;
    }

    public static boolean is盜賊(final int job) {
        return getJobBranch(job) == 4 || getJobBranch(job) == 6;
    }

    public static boolean is海盜(final int job) {
        return getJobBranch(job) == 5 || getJobBranch(job) == 6;
    }

    public static short getBeginner(final short job) {
        if (job % 1000 < 10) {
            return job;
        }
        switch (job / 100) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 8:
            case 9:
                return (short) MapleJob.初心者.getId();
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                return (short) MapleJob.貴族.getId();
            case 20:
                return (short) MapleJob.傳說.getId();
            case 21:
                return (short) MapleJob.傳說.getId();
            case 22:
                return (short) MapleJob.龍魔導士.getId();
            case 23:
                return (short) MapleJob.精靈遊俠.getId();
            case 24:
                return (short) MapleJob.幻影俠盜.getId();
            case 25:
                return (short) MapleJob.隱月.getId();
            case 27:
                return (short) MapleJob.夜光.getId();
            case 31:
                return (short) MapleJob.惡魔殺手.getId();
            case 36:
                return (short) MapleJob.傑諾.getId();
            case 30:
            case 32:
            case 33:
            case 35:
                return (short) MapleJob.市民.getId();
            case 40:
            case 41:
                return (short) MapleJob.劍豪.getId();
            case 42:
                return (short) MapleJob.陰陽師.getId();
            case 50:
            case 51:
                return (short) MapleJob.米哈逸.getId();
            case 60:
            case 61:
                return (short) MapleJob.凱撒.getId();
            case 65:
                return (short) MapleJob.天使破壞者.getId();
            case 100:
            case 101:
                return (short) MapleJob.神之子JR.getId();
            case 110:
            case 112:
                return (short) MapleJob.幻獸師.getId();
            case 130:
            case 131:
                return (short) MapleJob.皮卡啾.getId();
            case 140:
            case 142:
                return (short) MapleJob.凱內西斯.getId();
        }
        return (short) MapleJob.初心者.getId();
    }

    public static boolean isNotMpJob(int job) {
        return is惡魔(job) || is天使破壞者(job) || is神之子(job) || is陰陽師(job) || is凱內西斯(job);
    }

    public static boolean is初心者(int jobid) {
        if (jobid > 5000) {
            if (jobid > 13000) {
                if (jobid == 14000) {
                    return true;
                }
            } else if (jobid == 13000 || jobid >= 6000 && jobid <= 6001) {
                return true;
            }
        } else {
            if (jobid == 5000) {
                return true;
            }
            if (jobid > 3002) {
                if (jobid >= 4001 && jobid <= 4002) {
                    return true;
                }
            } else if (jobid >= 3001 || jobid >= 2001 && jobid <= 2005) {
                return true;
            }
        }
        if (jobid - 40000 <= 5 || jobid % 1000 > 0 && jobid - 800000 >= 100 && jobid != 8001 && jobid - 800100 >= 100) {
            return false;
        }
        return true;
    }

    public static boolean isJob8000(int job) {
        int v1 = SkillConstants.getJobBySkill(job);
        if ((v1 - 800000 <= 99 && v1 - 800000 >= 0) || v1 == 8001) {
            return true;
        } else {
            return v1 - 800100 <= 99 && v1 - 800100 >= 0;
        }
    }

    public static boolean isJob9500(int job) {
        boolean result;
        if (job >= 0) {
            result = SkillConstants.getJobBySkill(job) == 9500;
        } else {
            result = false;
        }
        return result;
    }

    public static int get龍魔轉數(int jobid) {
        switch (jobid) {
            case 2200:
            case 2210:
                return 1;
            case 2211:
            case 2212:
            case 2213:
                return 2;
            case 2214:
            case 2215:
            case 2216:
                return 3;
            case 2217:
            case 2218:
                return 4;
            default:
                return 0;
        }
    }

    public static int get轉數(int jobid) {
        int result;
        if (is初心者(jobid) || jobid % 100 == 0 || jobid == 501 || jobid == 3101 || jobid == 508) {
            result = 1;
        } else if (is龍魔導士(jobid)) {
            result = get龍魔轉數(jobid);
        } else if (jobid / 10 == 43) {
            result = 0;
            if ((jobid - 430) / 2 <= 2) {
                result = (jobid - 430) / 2 + 2;
            }
        } else {
            result = 0;
            if (jobid % 10 <= 2) {
                result = jobid % 10 + 2;
            }
        }
        return result;
    }

    public static boolean isBeginner(final int job) {
        return getJobGrade(job) == 0;
    }

    public static boolean isSameJob(int job, int job2) {
        int jobNum = getJobGrade(job);
        int job2Num = getJobGrade(job2);
        // 對初心者判斷
        if (jobNum == 0 || job2Num == 0) {
            return getBeginner((short) job) == getBeginner((short) job2);
        }

        // 初心者過濾掉后, 對職業群進行判斷
        if (getJobGroup(job) != getJobGroup(job2)) {
            return false;
        }

        // 代碼特殊的單獨判斷
        if (JobConstants.is管理員(job) || JobConstants.is管理員(job)) {
            return JobConstants.is管理員(job2) && JobConstants.is管理員(job2);
        } else if (JobConstants.is重砲指揮官(job) || JobConstants.is重砲指揮官(job)) {
            return JobConstants.is重砲指揮官(job2) && JobConstants.is重砲指揮官(job2);
        } else if (JobConstants.is蒼龍俠客(job) || JobConstants.is蒼龍俠客(job)) {
            return JobConstants.is蒼龍俠客(job2) && JobConstants.is蒼龍俠客(job2);
        } else if (JobConstants.is惡魔復仇者(job) || JobConstants.is惡魔復仇者(job)) {
            return JobConstants.is惡魔復仇者(job2) && JobConstants.is惡魔復仇者(job2);
        }

        // 對一轉分支判斷(如 劍士 跟 黑騎)
        if (jobNum == 1 || job2Num == 1) {
            return job / 100 == job2 / 100;
        }

        return job / 10 == job2 / 10;
    }

    public static int getJobGroup(int job) {
        return job / 1000;
    }

    public static int getJobBranch(int job) {
        if (job / 100 == 27) {
            return 2;
        } else {
            return job % 1000 / 100;
        }
    }

    public static int getJobBranch2nd(int job) {
        if (job / 100 == 27) {
            return 2;
        } else {
            return job % 1000 / 100;
        }
    }

    public static int getJobGrade(int jobz) {
        if (is龍魔導士(jobz)) {
            return get龍魔轉數(jobz);
        }
        int job = (jobz % 1000);
        if (job / 10 == 0) {
            return 0; //beginner
        } else if (job / 10 % 10 == 0) {
            return 1;
        } else {
            return job % 10 + 2;
        }
    }
}
