package com.msemu.world.constants;

/**
 * Created by Weber on 2018/3/31.
 */
public class JobConstants {
    public static boolean isSeparatedSp(int job) {
        return !MapleJob.is管理員(job) && !MapleJob.is幻獸師(job) && !MapleJob.is皮卡啾(job);
    }
}
