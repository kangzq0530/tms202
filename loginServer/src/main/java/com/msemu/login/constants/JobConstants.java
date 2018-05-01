package com.msemu.login.constants;

/**
 * Created by Weber on 2018/4/14.
 */
public class JobConstants {

    public static final boolean enableJobs = true;
    // UI.wz/login.img/RaceSelect_new/order
    public static final int jobOrder = 194;


    public static boolean isSeparatedSp(int job) {
        return !MapleJob.is管理員(job) && !MapleJob.is幻獸師(job) && !MapleJob.is皮卡啾(job);
    }
}
