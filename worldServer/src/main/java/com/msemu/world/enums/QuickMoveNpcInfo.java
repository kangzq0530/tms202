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

package com.msemu.world.enums;

import com.msemu.commons.utils.types.FileTime;
import lombok.Getter;

/**
 * Created by Weber on 2018/5/12.
 */
public enum QuickMoveNpcInfo {
    //PVP(0, 9070004, 30, "Move to the Battle Mode zone #cBattle Square#, where you can fight against other users.\n#cLv. 30 or above can participate in Battle Square."),
    怪物公園(1, 9071003, 20, "和隊員們齊心合力攻略強悍怪物的區域.\n移動到#c<怪物公園>#.\n#c一般怪物公園:  100級以上可參加\n 怪物競技場: 70級 ~ 200級"),
    次元之鏡(2, 9010022, 10, "使用可傳送到組隊任務等各種副本地圖的#c<次元之鏡>#。"),
    自由市場(3, 9000087, 0, "傳送到可與其他玩家進行道具交易的#c<自由市場>#。"),
    梅斯特鎮(4, 9000088, 30, "傳送到進行生產技術的#c<梅斯特鎮>#。\n#c35級以上就可進入。"),
    大陸移動碼頭(5, 9000086, 0, "傳送到最近的#c<大陸移動碼頭>#。"), //Boats, Airplanes
    計程車(6, 9000089, 0, "使用#c<計程車>#可將角色移動到附近主要地區。"), //Taxi, Camel
    //戴彼得(7, 9010040, 10, ""),
    //被派來的藍多普(8, 0, 10, ""),
    //被派來的露西亞(9, 0, 10, ""),
    打工(10, 9010041, 30, "獲得打工的酬勞。"),
    //末日風暴防具商店(11, 9010047, 30, ""),
    //末日風暴武器商店(12, 9010048, 30, ""),
    皇家美髮(13, 9000123, 0, "可以讓比克·艾德華為你修剪一頭帥氣的髮型。"),
    皇家整形(14, 9000124, 0, "可以讓Dr·塑膠洛伊為你進行整型手術。"),
    冬季限量防具商店(15, 9000152, 30, ""),
    冬季限量武器商店(16, 9000153, 30, ""),
    //        里貝卡(17, 9000018, 30, "可使用高級硬幣向里貝卡兌換各種道具裝備。"),
    琳(17, 9000365, 30, "能使用高級服務專用金幣跟琳購買道具。"),
    //QM_UNKNOW1(18, 0, 30, ""),//一个好像财主的兔子
    彌莎(19, 0, 10, ""),
    //楓之谷拍賣場(20, 0, 30, "可以透過愛格里曲來訪問楓之谷拍賣場."),//178已刪
    //戰國露西亞(21, 100, 9130033, 30, ""),//戰國商店
    //戰國藍多普(22, 101, 9130032, 30, ""),//戰國商店
    初音未來(23, 102, 812000000, 30, "移動至初音未來合作特設地圖#c<初音未來的演唱會會場>#。"),
    // 自訂快速移動
    次元傳送門(24, 2, 3000012, 10, "使用可傳送到任意城市的#c<次元傳送門>#。"),
    聚合功能(35, 19, 0, 10, "便利功能NPC."),;
    @Getter
    private final long value;
    @Getter
    private final int type, id, level;
    @Getter
    private final String desc;
    @Getter
    private final FileTime start;
    @Getter
    private final FileTime end;

    private QuickMoveNpcInfo(int type, int id, int level, String desc) {
        this.value = 1 << type;
        this.type = type;
        this.id = id;
        this.level = level;
        this.desc = desc;
        this.start = FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME);
        this.end = FileTime.getFileTimeFromType(FileTime.Type.MAX_TIME);
    }

    private QuickMoveNpcInfo(int value, int type, int id, int level, String desc) {
        this.value = 1 << value;
        this.type = type;
        this.id = id;
        this.level = level;
        this.desc = desc;
        this.start = FileTime.getFileTimeFromType(FileTime.Type.PERMANENT);
        this.end = FileTime.getFileTimeFromType(FileTime.Type.PERMANENT);
    }

    public final boolean check(long flag) {
        return (flag & value) != 0;
    }


    public boolean show(int mapId) {
        QuickMoveInfo quick = null;
        for (QuickMoveInfo q : QuickMoveInfo.values()) {
            if (q.getFieldID() == mapId) {
                quick = q;
                break;
            }
        }
        return (quick != null && check(quick.getNpcFlag())) || check(QuickMoveInfo.GLOBAL_NPC);
    }
}
