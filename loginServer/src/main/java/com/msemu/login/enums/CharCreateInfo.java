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

package com.msemu.login.enums;

import com.msemu.login.constants.MapleJob;
import lombok.Getter;

/**
 * Created by Weber on 2018/4/19.
 */
public enum CharCreateInfo {
    終極冒險家(-1, 0, MapleJob.初心者.getId(), 100000000, CharCreateItemFlag.褲裙.getVelue()),
    末日反抗軍(0, 0, 0, MapleJob.市民.getId(), 931000000),
    冒險家(1, 0, MapleJob.初心者.getId(), 4000011),
    皇家騎士團(2, 0, MapleJob.貴族.getId(), 130030000, CharCreateItemFlag.披風.getVelue()),
    狂狼勇士(3, 0, MapleJob.傳說.getId(), 914000000, CharCreateItemFlag.褲裙.getVelue()),
    龍魔導士(4, 0, MapleJob.龍魔導士.getId(), 900010000, CharCreateItemFlag.褲裙.getVelue()),
    精靈遊俠(5, 0, MapleJob.精靈遊俠.getId(), 910150000),
    惡魔(6, 0, MapleJob.惡魔殺手.getId(), 931050310, CharCreateItemFlag.臉飾.getVelue() | CharCreateItemFlag.副手.getVelue()),
    幻影俠盜(7, 0, MapleJob.幻影俠盜.getId(), 915000000, CharCreateItemFlag.披風.getVelue()),
    影武者(8, 0, 1, MapleJob.初心者.getId(), 103050900),
    米哈逸(9, 0, MapleJob.米哈逸.getId(), 913070000, CharCreateItemFlag.褲裙.getVelue()),
    夜光(10, 0, MapleJob.夜光.getId(), 927020080, CharCreateItemFlag.披風.getVelue()),
    凱撒(11, 0, MapleJob.凱撒.getId(), 940001000),
    天使破壞者(12, 0, MapleJob.天使破壞者.getId(), 940011000),
    重砲指揮官(13, 2, MapleJob.初心者.getId(), 3000600),
    傑諾(14, 0, MapleJob.傑諾.getId(), 931060089, CharCreateItemFlag.臉飾.getVelue()),
    神之子(15, 0, MapleJob.神之子.getId(), 321000001, CharCreateItemFlag.披風.getVelue() | CharCreateItemFlag.副手.getVelue()),
    隱月(16, 0, MapleJob.隱月.getId(), 927030050, CharCreateItemFlag.褲裙.getVelue() | CharCreateItemFlag.披風.getVelue()),
    皮卡啾(17, 0, MapleJob.皮卡啾1轉.getId(), 927030090),
    凱內西斯(18, 0, MapleJob.凱內西斯.getId(), 331001100),
    蒼龍俠客(19, 0, MapleJob.初心者.getId(), 743020100, CharCreateItemFlag.褲裙.getVelue()),
    劍豪(20, 0, MapleJob.劍豪.getId(), 807100010, CharCreateItemFlag.帽子.getVelue() | CharCreateItemFlag.手套.getVelue()),
    陰陽師(21, 0, MapleJob.陰陽師.getId(), 807100110, CharCreateItemFlag.帽子.getVelue() | CharCreateItemFlag.手套.getVelue()),
    幻獸師(22, 0, MapleJob.幻獸師.getId(), 866100000, CharCreateItemFlag.臉飾.getVelue() | CharCreateItemFlag.耳朵.getVelue() | CharCreateItemFlag.尾巴.getVelue());

    @Getter
    public final int flag;
    @Getter
    private final int jobType, subJob, id, map;
    private boolean enableCreate = true;

    CharCreateInfo(int jobType, int subJob, int id, int map) {
        this(jobType, subJob, id, map, 0);
    }

    CharCreateInfo(int jobType, int subJob, int id, int map, int flag) {
        this(jobType, subJob, id, map, 0, true);
    }

    CharCreateInfo(int jobType, int subJob, int id, int map, int flag, boolean enableCreate) {
        this.jobType = jobType;
        this.subJob = subJob;
        this.id = id;
        this.map = map;
        this.flag = flag | CharCreateItemFlag.臉型.getVelue() | CharCreateItemFlag.髮型.getVelue() | CharCreateItemFlag.衣服.getVelue() | CharCreateItemFlag.鞋子.getVelue() | CharCreateItemFlag.武器.getVelue();
        this.enableCreate = true;
    }

    public static CharCreateInfo getByJobType(int jobType) {
        for (CharCreateInfo e : CharCreateInfo.values()) {
            if (e.jobType == jobType) {
                return e;
            }
        }
        return null;
    }

    public static CharCreateInfo getById(int g) {
        for (CharCreateInfo e : CharCreateInfo.values()) {
            if (e.id == g) {
                return e;
            }
        }
        return null;
    }

    public static boolean checkEnable(int jobType) {
        for (CharCreateInfo info : CharCreateInfo.values()) {
            if (info.getJobType() == jobType) {
                return info.enableCreate();
            }
        }
        return false;
    }

    public boolean enableCreate() {
        return enableCreate;
    }

    public void setEnableCreate(boolean enableCreate) {
        this.enableCreate = enableCreate;
    }
}
