package com.msemu.login.enums;

/**
 * Created by Weber on 2018/4/14.
 */
public enum  LoginJob {

    末日反抗軍(0,true),

    冒險家(1,true),

    皇家騎士團(2,true),

    狂狼勇士(3,true),

    龍魔導士(4,true),

    精靈遊俠(5,true),

    惡魔(6,true),

    幻影俠盜(7,true),

    影武者(8,true),

    米哈逸(9,true),

    夜光(10,true),

    凱撒(11,true),

    天使破壞者(12,true),

    重砲指揮官(13,true),

    傑諾(14,true),

    神之子(15,true),

    隱月(16,true),

    皮卡啾(17,true),

    凱內西斯(18,true),

    蒼龍俠客(19,true),

    劍豪(20,true),

    陰陽師(21,true),

    幻獸師(22,true),;

    private final int jobType;
    private boolean enableCreate = true;

    private LoginJob(int jobType, boolean enableCreate) {
        this.jobType = jobType;
        this.enableCreate = enableCreate;
    }

    public int getJobType() {
        return jobType;
    }

    public boolean enableCreate() {
        return enableCreate;
    }

    public void setEnableCreate(boolean enableCreate) {
        this.enableCreate = enableCreate;
    }
}

