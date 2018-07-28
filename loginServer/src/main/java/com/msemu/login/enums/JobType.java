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

/**
 * Created by Weber on 2018/4/19.
 */
public enum  JobType {

    末日反抗軍(0),

    冒險家(1),

    皇家騎士團(2),

    狂狼勇士(3),

    龍魔導士(4),

    精靈遊俠(5),

    惡魔(6),

    幻影俠盜(7),

    影武者(8),

    米哈逸(9),

    夜光(10),

    凱撒(11),

    天使破壞者(12),

    重砲指揮官(13),

    傑諾(14),

    神之子(15),

    隱月(16),

    皮卡啾(17),

    凱內西斯(18),

    蒼龍俠客(19),

    劍豪(20),

    陰陽師(21),

    幻獸師(22),
    ;
    int jobType;

    JobType(int jobType) {
        this.jobType = jobType;
    }
}
