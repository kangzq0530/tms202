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

/**
 * Created by Weber on 2018/5/12.
 */
public class FieldConstants {
    public static boolean isBossMap(int mapid) {
        switch (mapid) {
            case 0:
            case 105100300:
            case 105100400:
            case 211070100:
            case 211070101:
            case 211070110:
            case 220080001:
            case 240040700:
            case 240060200:
            case 240060201:
            case 270050100:
            case 271040100:
            case 271040200:
            case 280030000:
            case 280030001:
            case 280030100:
            case 300030310:
            case 551030200:
            case 802000111:
            case 802000211:
            case 802000311:
            case 802000411:
            case 802000611:
            case 802000711:
            case 802000801:
            case 802000802:
            case 802000803:
            case 802000821:
            case 802000823:
                return true;
        }
        return false;
    }

    public static boolean isTutorialMap(int mapid) {
        if (mapid < 100000000) { //冒險家 & 重砲指揮官Explorer & Cannoneer
            return true;
        } else if (mapid / 10000 == 93100) { //末日反抗軍Resistance
            return true;
        } else if (mapid / 10000 == 13003) { //皇家騎士團Cygnus
            return true;
        } else if (mapid / 10000 == 91400 || mapid / 10000 == 14009) { //狂狼勇士Aran
            return true;
        } else if (mapid / 100000 == 9000 || mapid / 10000 == 10003) { //龍魔導士Evan
            return true;
        } else if (mapid / 10000 == 91015) { //精靈遊俠Mercedes
            return true;
        } else if (mapid / 10000 == 93105) { //惡魔殺手Demon Slayer
            return true;
        } else if (mapid / 10000 == 91500) { //幻影俠盜Phantom
            return true;
        } else if (mapid / 100 == 1030509 || mapid / 100 == 1030505) { //影武者Dual Blade
            return true;
        } else if (mapid / 10000 == 91307) { //米哈逸Mihile
            return true;
        } else if (mapid / 10000 == 92702 || mapid / 1000 == 910141) { //夜光
            return true;
        } else if (mapid / 10000 == 94000) { //凱撒Kaiser
            return true;
        } else if (mapid / 10000 == 94001) { //天使破壞者Angelic
            return true;
        } else if (mapid / 10000 == 91206) { //重砲指揮官Cannoneer
            return true;
        } else if (mapid / 100000 == 7430) { //蒼龍Jett
            return true;
        } else if (mapid / 10 == 9270300 || mapid / 10 == 9402000) { //隱月
            return true;
        } else if (mapid / 100000 == 3210 || mapid / 100000 == 3211) { //神之子Zero
            return true;
        } else if (mapid / 100 == 8071000) { //劍豪Hayato
            return true;
        } else if (mapid / 100 == 8071001) { //陰陽師Kanna
            return true;
        } else if (mapid / 100000 == 8661) { //幻獸師Beast Tamer
            return true;
        } else if (mapid / 10 == 92703009) { //皮卡啾Pink Bean
            return true;
        } else if (mapid / 10000 == 33100) { // 凱內西斯
            return true;
        }
        return false;
        //There might be included other maps like main town or job advancements,
        //But we don't care since you don't get much exp here and you're locked on teasers.
    }
}
